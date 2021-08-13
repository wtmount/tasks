package collections

import scala.io.Source

object CollectionsMain extends App {
  val lines = Source.fromResource("atussum.csv").getLines.take(10000)
  val headers = lines.next.split(",").map(_.trim).toList
  val rows = lines.toArray.map(_.split(",").map(_.trim)).map(_.zipWithIndex)

  val primaryNeedsIndices = findIndices(List("t01", "t03", "t11", "t1801", "t1803"))
  val workingActivitiesIndices = findIndices(List("t05", "t1805"))
  val leisureIndices = findIndices(List("t02", "t04", "t06", "t07", "t08", "t09", "t10", "t12", "t13", "t14", "t15", "t16", "t18"))
    .filterNot(i => primaryNeedsIndices.contains(i) || workingActivitiesIndices.contains(i))

  printf("Average time spent on primary needs compared to leisure time%n  primary needs - %.2f%n  leisure time - %.2f%n%n",
    calculateAverageTimeForActivity(rows, primaryNeedsIndices), calculateAverageTimeForActivity(rows, leisureIndices))
  printf("Women spend %.2f hours on work while men spend %.2f hours on work%n%n",
    calculateAverageTimeForActivityForGroup(rows, primaryNeedsIndices, "tesex", _ != 1),
    calculateAverageTimeForActivityForGroup(rows, primaryNeedsIndices, "tesex", _ == 1))
  printf("Time spent on primary needs%n  young people - %.2f%n  active people - %.2f%n  elder people - %.2f%n%n",
    calculateAverageTimeForActivityForGroup(rows, primaryNeedsIndices, "teage", a => 15 <= a && a <= 22),
    calculateAverageTimeForActivityForGroup(rows, primaryNeedsIndices, "teage", a => 23 <= a && a <= 55),
    calculateAverageTimeForActivityForGroup(rows, primaryNeedsIndices, "teage", a => a > 55))
  printf("Leisure time of working people compared to unemployed people:%n  working people - %.2f%n  unemployed people - %.2f%n",
    calculateAverageTimeForActivityForGroup(rows, leisureIndices, "telfs", i => 1 <= i && i < 3),
    calculateAverageTimeForActivityForGroup(rows, leisureIndices, "telfs", _ == 5))

  def findIndices(columnNames: List[String]): List[Int] =
    headers.filter(h => columnNames.exists(cn => h.startsWith(cn))).map(headers.indexOf(_))

  def calculateAverageTimeForActivityForGroup(rows: Array[Array[(String, Int)]], indices: List[Int], filterColumn: String, f: Int => Boolean): Double =
    calculateAverageTimeForActivity(rows.filter(r => f(r(headers.indexOf(filterColumn))._1.toInt)), indices)

  def calculateAverageTimeForActivity(rows: Array[Array[(String, Int)]], indices: List[Int]): Double = {
    val res = calculateTimeForActivityForEachRow(rows, indices)
    res.sum.toDouble / res.length
  }

  def calculateTimeForActivityForEachRow(rows: Array[Array[(String, Int)]], indices: List[Int]): Array[Int] =
    rows.map(_.filter(i => indices.contains(i._2)).map(_._1.toInt).sum)
}