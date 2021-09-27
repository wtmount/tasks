package mapreduce

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar

import scala.collection.mutable
import scala.util.Random

class FrequencyHistogramTest extends TestKit(ActorSystem("MainFrequencyHistogram"))
  with ImplicitSender with AnyWordSpecLike with BeforeAndAfterAll with MockitoSugar {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Mapper" must {
    "return a CountedWords message with counted words in a chunk of text" in {
      val randomMock = mock[Random]
      when(randomMock.nextInt(any())).thenReturn(0)
      val mapper = system.actorOf(Props[Mapper](new  Mapper(randomMock)))
      mapper ! CountWordsInChunk(1, Array("to be", "or not to be"))
      expectMsg(CountedWords(1, mutable.HashMap("be" -> 2, "not" -> 1, "or" -> 1, "to" -> 2)))
    }
  }

  "Mapper" must {
    "return a FailedMapping message when wrong number is obtained" in {
      val randomMock = mock[Random]
      when(randomMock.nextInt(any())).thenReturn(1)
      val mapper = system.actorOf(Props[Mapper](new Mapper(randomMock)))
      val chunk = Array("to be", "or not to be")
      mapper ! CountWordsInChunk(1, chunk)
      expectMsg(FailedMapping(1, chunk))
    }
  }

  "Reducer" must {
    "return a TotalWordCount message with a map of total word count after reducing a counted chunk of text" in {
      val reducer = system.actorOf(Props[Reducer])
      val chunk1 = mutable.HashMap("be" -> 2, "not" -> 1, "or" -> 1, "to" -> 2)
      val chunk2 = mutable.HashMap("is" -> 1, "question" -> 1, "that" -> 1, "the" -> 1)
      reducer ! CountedWords(1, chunk1)
      expectMsg(TotalWordCount(1, chunk1))
      reducer ! CountedWords(2, chunk2)
      expectMsg(TotalWordCount(2, mutable.HashMap("be" -> 2, "is" -> 1, "not" -> 1, "or" -> 1, "question" -> 1, "that" -> 1, "the" -> 1, "to" -> 2)))
    }
  }
}
