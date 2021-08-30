package mapreduce

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import java.io.{BufferedWriter, File, FileWriter}
import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.io.{BufferedSource, Source}
import scala.language.postfixOps

case object ProcessFile
case class CountedLine(words: mutable.Map[String, Int])
case object WriteResult
case object GetResult
case class Result(histogram: mutable.Map[String, Int])

class MapActor extends Actor {
  def receive = {
    case line: String =>
      val words = mutable.TreeMap[String, Int]()
      line.split("[\\s.,:;!?()*'\"\\[\\]]+").map(_.toLowerCase())
        .foreach(word => words.updateWith(word) {
          case Some(count) => Some(count + 1)
          case None => Some(1)
        })
      sender ! CountedLine(words)
  }
}

class ReduceActor extends Actor {
  val result = new mutable.TreeMap[String, Int]()
  def receive = {
    case CountedLine(words) =>
      words.foreach(word => result.updateWith(word._1) {
        case Some(count) => Some(count + word._2)
        case None => Some(word._2)
      })
    case GetResult => sender ! Result(result)
  }
}

class MapReduce(size: Int, in: BufferedSource, out: BufferedWriter, reduce: ActorRef) extends Actor {
  (1 to size).foreach(i => context.actorOf(Props[MapActor], s"mapper-$i"))

  private implicit val timeout: Timeout = new Timeout(Duration(5, SECONDS))
  private implicit val executionContext: ExecutionContext = context.dispatcher

  private var nextIndex = 0

  def receive = {
    case ProcessFile =>
      for (line <- in.getLines) {
        if (nextIndex >= size) nextIndex = 0
        (context.children.toIndexedSeq(nextIndex) ? line).pipeTo(reduce)
        nextIndex += 1
      }
    case WriteResult => reduce ! GetResult
    case Result(histogram) =>
      out.write(histogram.mkString(System.lineSeparator()))
      out.close()
  }
}

object FrequencyHistogram extends App {
  val system = ActorSystem("MapReduceMain")

  implicit val timeout: Timeout = new Timeout(Duration(5, SECONDS))
  implicit val executionContext: ExecutionContext = system.dispatcher

  val in = Source.fromResource("t8.shakespeare.txt")
  val out = new BufferedWriter(new FileWriter(new File("result.txt")))
  val reduce = system.actorOf(Props[ReduceActor], name = "reduceactor")
  val mapReduce = system.actorOf(Props(new MapReduce(10, in, out, reduce)), name = "MapReduce")

  mapReduce ! ProcessFile
  Thread.sleep(20000)
  mapReduce ! WriteResult

  system.terminate()
}
