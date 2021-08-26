package implicits

class CsvEncoder[A] {
  def toCSV[T: Ordering](records: List[T]): String = records.sorted(implicitly[Ordering[T]]).mkString(",")
}

object Orderings {
  implicit def ordering[A](implicit ev$1: A => Comparable[A]): Ordering[A] = (x: A, y: A) => y compareTo x
}