package fp

import org.scalatest.flatspec.AnyFlatSpec

class ReduceTest extends AnyFlatSpec {

  behavior of "Reduce"

  it should "reduce sequence of strings" in {
    assert(Reduce.reduce(Seq("a", "b", "c")) == "abc")
  }

  it should "reduce sequence of integers" in {
    assert(Reduce.reduce(Seq(1, 2, 3)) == 6)
  }
}
