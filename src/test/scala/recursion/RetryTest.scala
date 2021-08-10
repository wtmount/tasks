package recursion

import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec

class RetryTest extends AnyFlatSpec with MockFactory {

  behavior of "RetryTest"

  it should "return result when result is accepted" in {
    val stopper = stub[Stopper]
    val retry = new Retry(stopper)
    assert(retry.retry[Int](() => 1 + 2, _ > 0, List(100, 1000, 2000)) == Option[Int](3))
    (stopper.pause _).verify(*).never()
  }

  it should "return result when result is accepted in subsequent call" in {
    val stopper = stub[Stopper]
    val retry = new Retry(stopper)
    var i = -1
    assert(retry.retry[Int](() => { i += 1; i }, _ > 0, List(100, 1000, 2000)) == Option[Int](1))
    (stopper.pause _).verify(100).once()
    (stopper.pause _).verify(1000).never()
    (stopper.pause _).verify(2000).never()
  }

  it should "return None when result is not accepted and number of retries runs out" in {
    val stopper = stub[Stopper]
    val retry: Retry = new Retry(stopper)
    assert(retry.retry[Int](() => 1 - 2, _ > 0 , List(100, 1000, 2000)).isEmpty)
    (stopper.pause _).verify(100).once()
    (stopper.pause _).verify(1000).once()
    (stopper.pause _).verify(2000).once()
  }
}
