package mapreduce

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props}
import akka.routing.RoundRobinPool

import java.io.{BufferedWriter, File, FileWriter}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.DurationInt
import scala.io.{BufferedSource, Source}
import scala.language.postfixOps
import scala.util.Random

case object CountWordsInFile
case class CountWordsInChunk(chunkIndex: Int, chunk: Array[String])
case class CountedWords(chunkIndex: Int, countedWords: mutable.HashMap[String, Int])
case class FailedMapping(chunkIndex: Int, chunk: Array[String])
case class TotalWordCount(chunkIndex: Int, totalCount: mutable.HashMap[String, Int])

object FrequencyHistogram extends App {
  val system = ActorSystem("MainFrequencyHistogram")
  val in = Source.fromResource("t8.shakespeare.txt")
  val out = new BufferedWriter(new FileWriter(new File("histogram.txt")))
  val mapReducer = system.actorOf(Props(new MapReducer(in, out, 10, 5, system)), name = "mapReducer")
  mapReducer ! CountWordsInFile
}

class MapReducer(in: BufferedSource, out: BufferedWriter, numberOfChunks: Int, numberOfActors: Int, system: ActorSystem) extends Actor {
  private val reducer = context.actorOf(Props[Reducer], name = "reducer")
  private val countedChunks = ListBuffer.from(for (i <- 1 to numberOfChunks) yield i)
  private val strategy = OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 1 second) {
    case _ => Restart
  }
  private val router = context.actorOf(RoundRobinPool(numberOfActors, supervisorStrategy = strategy).props(Props[Mapper](new Mapper(Random))),"router")

  def receive = {
    case CountWordsInFile =>
      val lines = in.getLines().toArray
      val chunkSize = lines.length / numberOfChunks
      var currentPosition = 0
      for (i <- 1 until numberOfChunks) {
        router ! CountWordsInChunk(i, lines.slice(currentPosition, currentPosition + chunkSize))
        currentPosition += chunkSize
      }
      router ! CountWordsInChunk(numberOfChunks, lines.slice(currentPosition, lines.length))

    case cw @ CountedWords(_, _) => reducer ! cw

    case FailedMapping(chunkIndex, chunk) => router ! CountWordsInChunk(chunkIndex, chunk)

    case TotalWordCount(chunkIndex, totalWordCount) =>
      countedChunks -= chunkIndex
      if (countedChunks.isEmpty) {
        out.write(totalWordCount.mkString(System.lineSeparator))
        out.close()
        system.terminate()
      }
  }
}

class Mapper(random: Random) extends Actor {
  def receive = {
    case CountWordsInChunk(chunkIndex, chunk) =>
      val countedWords = mutable.HashMap[String, Int]()
      chunk.flatMap(line => line.split("[\\s.,:;!?()*'\"\\[\\]]+").map(_.toLowerCase))
        .foreach(word => countedWords.updateWith(word) {
          case Some(count) => Some(count + 1)
          case None => Some(1)
        })
      val wrongNumber = random.nextInt(200)
      if (countedWords.values.exists(_ == wrongNumber)) {
        sender ! FailedMapping(chunkIndex, chunk)
        throw new RuntimeException
      } else {
        sender ! CountedWords(chunkIndex, countedWords)
      }
  }
}

class Reducer extends Actor {
  private val totalWordCount = mutable.HashMap[String, Int]()
  def receive = {
    case CountedWords(chunkIndex, countedWords) =>
      countedWords.foreach(word => totalWordCount.updateWith(word._1) {
        case Some(count) => Some(count + word._2)
        case None => Some(word._2)
      })
      sender ! TotalWordCount(chunkIndex, totalWordCount)
  }
}