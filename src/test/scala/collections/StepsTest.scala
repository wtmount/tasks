package collections

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec

class StepsTest extends AnyFlatSpec with BeforeAndAfterEach {
  val steps = new Steps

  behavior of "Steps"

  it should "return 1 as possible number of ways to get to step 0" in {
    val result = steps.countSteps().take(1)
    assert(result(0) == 1)
  }

  it should "return number of possible ways to get to step 1" in {
    val result = steps.countSteps().take(2)
    assert(result(1) == 0)
  }

  it should "return number of possible ways to get to Nth step" in {
    val result = steps.countSteps().take(11)
    assert(result(10) == 14)
  }
}
