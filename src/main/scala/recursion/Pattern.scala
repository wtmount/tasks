package recursion

import scala.annotation.tailrec

object Pattern {
  val Diff = 4

  def findPattern(n: Int): String = {
    buildList(n, Nil).mkString(",")
  }

  @tailrec
  private def buildList(n: Int, l: List[Int]): List[Int] = {
    val t = l.splitAt(l.length / 2)
    if (n <= 0) t._1 ::: n :: t._2 else buildList(n - Diff, t._1 ::: n :: n :: t._2)
  }
}
