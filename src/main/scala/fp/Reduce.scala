package fp

object Reduce {
  trait Monoid[T] {
    def empty: T
    def add(x: T, y: T): T
  }

  object Monoid {
    implicit object stringMonoid extends Monoid[String] {
      override def empty: String = ""
      override def add(x: String, y: String): String = x ++ y
    }

    implicit object intMonoid extends Monoid[Int] {
      override def empty: Int = 0
      override def add(x: Int, y: Int): Int = x + y
    }
  }

  def reduce[T: Monoid](seq: Seq[T]): T = {
    if (seq.isEmpty) implicitly[Monoid[T]].empty else implicitly[Monoid[T]].add(seq.head, reduce(seq.tail))
  }
}
