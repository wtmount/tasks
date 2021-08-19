package collections

class Steps {
  val steps = List(2, 3, 5)

  def countSteps(): LazyList[Int] = 1 #:: count(LazyList(1))

  private def count(l: LazyList[Int] = LazyList.empty): LazyList[Int] = {
    val ways = steps.map(s => if (l.length - s >= 0) l(l.length - s) else 0).sum
    ways #:: count(l :+ ways)
  }
}
