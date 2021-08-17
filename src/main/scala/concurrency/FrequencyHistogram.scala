package concurrency

import java.io.{BufferedWriter, File, FileWriter}
import scala.collection.concurrent.TrieMap
import scala.collection.parallel.CollectionConverters.ImmutableIterableIsParallelizable
import scala.io.Source

object FrequencyHistogram extends App {
  val src = Source.fromResource("t8.shakespeare.txt")
  val wordCount = TrieMap[String, Int]()

  src.getLines.toList.par
    .flatMap(_.split("[\\s.,:;!?()'\"\\[\\]]+").map(_.toLowerCase()))
    .foreach(w => wordCount.updateWith(w) {
      case Some(c) => Some(c + 1)
      case None => Some(1)
    })

  val histogram = for (i <- wordCount) yield f"${i._1}%-20s${"*" * i._2}"
  val out = new BufferedWriter(new FileWriter(new File("histogram.txt")))
  out.write(histogram.mkString(System.lineSeparator))
  out.close()
}
