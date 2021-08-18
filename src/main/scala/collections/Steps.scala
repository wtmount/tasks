package collections

object Steps extends App {
  val N = 10
  println(countSteps(N).size)

  def countSteps(n: Int): LazyList[Int] = {
    if (n < 0) LazyList.empty
    else if (n == 0) LazyList(1)
    else countSteps(n - 2) #::: countSteps(n - 3) #::: countSteps(n - 5)
  }
}
