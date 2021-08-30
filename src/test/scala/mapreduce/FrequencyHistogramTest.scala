package mapreduce

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.mutable

class FrequencyHistogramTest extends TestKit(ActorSystem("MapReduceMain"))
  with ImplicitSender with AnyWordSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "MapActor" must {
    "return a map with counted words in a line" in {
      val actor = system.actorOf(Props[MapActor])
      actor ! "to be or not to be"
      expectMsg(CountedLine(mutable.TreeMap("be" -> 2, "not" -> 1, "or" -> 1, "to" -> 2)))
    }
  }

  "ReduceActor" must {
    "reduce multiple counted lines into one map" in {
      val actor = system.actorOf(Props[ReduceActor])
      actor ! CountedLine(mutable.TreeMap("be" -> 2, "not" -> 1, "or" -> 1, "to" -> 2))
      actor ! CountedLine(mutable.TreeMap("is" -> 1, "question" -> 1, "that" -> 1, "the" -> 1))
      actor ! GetResult
      expectMsg(Result(mutable.TreeMap("be" -> 2, "is" -> 1, "not" -> 1, "or" -> 1, "question" -> 1, "that" -> 1, "the" -> 1, "to" -> 2)))
    }
  }
}
