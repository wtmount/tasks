package implicits

import scala.language.implicitConversions

case class Rational(n: Long, d: Long) {
  def +(that: Rational): Rational = Rational(n * that.d + that.n * d, d * that.d)
  def -(that: Rational): Rational = Rational(n * that.d - that.n * d, d * that.d)
  def *(that: Rational): Rational = Rational(n * that.n, d * that.d)
  def /(that: Rational): Rational = Rational(n * that.d, d * that.n)
  def max(that: Rational): Rational = if (this > that) this else that
  def min(that: Rational): Rational = if (this > that) that else this

  private def >(that: Rational): Boolean = n * that.d > that.n * d
}

object Rational {
  def apply(n: Long, d: Long): Rational = {
    val g = gcd(n, d)
    new Rational(n / g, d / g)
  }

  def apply(n: Long): Rational = Rational(n, 1)

  private def gcd(n: Long, d: Long): Long = if (d == 0) n else gcd(d, n % d)

  implicit def intToRational(i: Int): Rational = Rational(i)
  implicit def longToRational(l: Long): Rational = Rational(l)
}