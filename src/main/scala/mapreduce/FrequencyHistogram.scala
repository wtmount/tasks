package mapreduce

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}
import akka.pattern.ask
import akka.util.Timeout

import java.io.{BufferedWriter, File, FileWriter}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.io.Source
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}

case class CountWordsInFile(lines: Array[String])
case class CountWordsInChunk(chunkIndex: Int, chunk: Array[String])
case class CountedWords(chunkIndex: Int, countedWords: Map[String, Int])
case class TotalWordCount(chunkIndex: Int, totalCount: mutable.HashMap[String, Int])

object FrequencyHistogram extends App {
  val system = ActorSystem("MainFrequencyHistogram")
  val lines = Source.fromResource("t8.shakespeare.txt").getLines.toArray
  val out = new BufferedWriter(new FileWriter(new File("histogram.txt")))
  val mapReducer = system.actorOf(Props(new MapReducer(10, 5)), name = "mapReducer")

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(10 second)

  (mapReducer ? CountWordsInFile(lines)).onComplete {
    case Success(result: mutable.HashMap[_, _]) =>
      out.write(result.mkString(System.lineSeparator))
      out.close()
      system.terminate()
    case Failure(f) =>
      out.write("Failed to process file...")
      out.close()
      system.terminate()
  }
}

class MapReducer(numberOfChunks: Int, numberOfActors: Int) extends Actor {
  (1 to numberOfActors).foreach(i => context.actorOf(Props[Mapper](new Mapper(Random)), s"$i"))
  private val reducer = context.actorOf(Props[Reducer], name = "reducer")
  private val countedChunks = ListBuffer.from(1 to numberOfChunks)
  private var jobCreator: ActorRef = _
  private var nextIndex = 0
  override val supervisorStrategy: OneForOneStrategy = OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 100 millisecond) {
    case _: RuntimeException => Restart
  }

  def receive = {
    case CountWordsInFile(lines) =>
      jobCreator = sender
      val chunkSize = lines.length / numberOfChunks
      var currentPosition = 0
      for (i <- 1 until numberOfChunks) {
        if (nextIndex >= numberOfActors) nextIndex = 0
        val child = context.children.toIndexedSeq(nextIndex)
        child ! CountWordsInChunk(i, lines.slice(currentPosition, currentPosition + chunkSize))
        nextIndex += 1
        currentPosition += chunkSize
      }
      if (nextIndex >= numberOfActors) nextIndex = 0
      context.children.toIndexedSeq(nextIndex) ! CountWordsInChunk(numberOfChunks, lines.slice(currentPosition, lines.length))

    case cw @ CountedWords(_, _) => reducer ! cw

    case TotalWordCount(chunkIndex, totalWordCount) =>
      countedChunks -= chunkIndex
      if (countedChunks.isEmpty) jobCreator ! totalWordCount
  }
}

class Mapper(random: Random) extends Actor {
  def receive = {
    case CountWordsInChunk(chunkIndex, chunk) =>
      val countedWords = chunk.flatMap(line => line.split("[\\s.,:;!?()*'\"\\[\\]]+").map(_.toLowerCase)).groupBy(identity).view.mapValues(_.length).toMap
      val wrongNumber = random.nextInt(200)
      if (countedWords.values.exists(_ == wrongNumber)) {
        throw new RuntimeException
      } else {
        sender ! CountedWords(chunkIndex, countedWords)
      }
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    message.foreach(m => self.tell(m, context.parent))
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