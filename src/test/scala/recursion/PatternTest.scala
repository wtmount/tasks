package recursion

import org.scalatest.flatspec.AnyFlatSpec

class PatternTest extends AnyFlatSpec {

  behavior of "PatternTest"

  it should "return numbers down to 0 or first negative and back to input when input is positive" in {
    assert(Pattern.findPattern(13) == "13,9,5,1,-3,1,5,9,13")
  }

  it should "return only 0 when 0 provided" in {
    assert(Pattern.findPattern(0) == "0")
  }

  it should "return only the number itself when a negative number provided" in {
    assert(Pattern.findPattern(-1) == "-1")
  }
}
