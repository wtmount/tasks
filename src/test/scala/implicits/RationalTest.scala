package implicits

import Rational._

import org.scalatest.flatspec.AnyFlatSpec

class RationalTest extends AnyFlatSpec {
  behavior of "Rational"

  it should "add two rationals" in {
    assert((Rational(2, 3) + Rational(5, 6)) == Rational(3, 2))
  }

  it should "add Long to rational" in {
    assert((Rational(2, 3) + 2L) == Rational(8, 3))
  }

  it should "add Int to rational" in {
    assert((Rational(2, 3) + 2) == Rational(8, 3))
  }

  it should "add rational to Long" in {
    assert((2L + Rational(2, 3)) == Rational(8, 3))
  }

  it should "add rational to Int" in {
    assert((2 + Rational(2, 3)) == Rational(8, 3))
  }

  it should "subtract rational from rational" in {
    assert((Rational(5, 6) - Rational(2, 3)) == Rational(1, 6))
  }

  it should "subtract Long from rational" in {
    assert((Rational(3, 2) - 1L) == Rational(1, 2))
  }

  it should "subtract Int from rational" in {
    assert((Rational(3, 2) - 1) == Rational(1, 2))
  }

  it should "subtract rational from Long" in {
    assert((1L - Rational(2, 3)) == Rational(1, 3))
  }

  it should "subtract rational from Int" in {
    assert((1 - Rational(2, 3)) == Rational(1, 3))
  }

  it should "multiply rational by rational" in {
    assert((Rational(2, 3) * Rational(3, 4)) == Rational(1, 2))
  }

  it should "multiply rational by Long" in {
    assert((Rational(2, 3) * 2L) == Rational(4, 3))
  }

  it should "multiply rational by Int" in {
    assert((Rational(2, 3) * 2) == Rational(4, 3))
  }

  it should "multiply Long by rational" in {
    assert((2L * Rational(2, 3)) == Rational(4, 3))
  }

  it should "multiply Int by rational" in {
    assert((2 * Rational(2, 3)) == Rational(4, 3))
  }

  it should "divide rational by rational" in {
    assert((Rational(2, 3) / Rational(3, 4)) == Rational(8, 9))
  }

  it should "divide rational by Long" in {
    assert((Rational(2, 3) / 3L) == Rational(2, 9))
  }

  it should "divide rational by Int" in {
    assert((Rational(2, 3) / 3) == Rational(2, 9))
  }

  it should "divide Long by rational" in {
    assert((3L / Rational(2, 3)) == Rational(9, 2))
  }

  it should "divide Int by rational" in {
    assert((3 / Rational(2, 3)) == Rational(9, 2))
  }

  it should "return larger of two rationals" in {
    assert((Rational(2, 3) max Rational(1, 2)) == Rational(2, 3))
  }

  it should "return larger of rational and Long" in {
    assert((Rational(9, 2) max 4L) == Rational(9, 2))
  }

  it should "return larger of rational and Int" in {
    assert((Rational(9, 2) max 4) == Rational(9, 2))
  }

  it should "return larger of Long and rational" in {
    assert((4L max Rational(9, 2)) == Rational(9, 2))
  }

  it should "return larger of Int and rational" in {
    assert((4 max Rational(9, 2)) == Rational(9, 2))
  }

  it should "return smaller of two rationals" in {
    assert((Rational(2, 3) min Rational(1, 2)) == Rational(1, 2))
  }

  it should "return smaller of rational and Long" in {
    assert((Rational(9, 2) min 4L) == Rational(4, 1))
  }

  it should "return smaller of rational and Int" in {
    assert((Rational(9, 2) min 4) == Rational(4, 1))
  }

  it should "return smaller of Long and rational" in {
    assert((4l min Rational(9, 2)) == Rational(4L))
  }

  it should "return smaller of Int and rational" in {
    assert((4 min Rational(9, 2)) == Rational(4))
  }
}
