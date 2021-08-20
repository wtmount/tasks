package collections

import scala.io.Source

object Survey extends App {
  case class Row(values: Map[String, String])

  val lines = Source.fromResource("atussum.csv").getLines.take(10000)
  val headers = lines.next.split(",").map(_.trim).toList
  val rows = lines.toList.map(_.split(",")).map(r => Row((headers zip r).toMap))

  val primaryNeedsColumns = findColumns(List("t01", "t03", "t11", "t1801", "t1803"))
  val workingColumns = findColumns(List("t05", "t1805"))
  val leisureColumns = findColumns(List("t02", "t04", "t06", "t07", "t08", "t09", "t10", "t12", "t13", "t14", "t15", "t16", "t18"))
    .filterNot(i => primaryNeedsColumns.contains(i) || workingColumns.contains(i))

  printf("Average time spent on primary needs compared to leisure time%n  primary needs - %.2f%n  leisure time - %.2f%n%n",
    calculateTime(primaryNeedsColumns, _ => true), calculateTime(leisureColumns, _ => true))
  printf("Women spend %.2f hours on work while men spend %.2f hours on work%n%n",
    calculateTime(workingColumns, _("tesex").toInt != 1), calculateTime(workingColumns, _("tesex").toInt == 1))
  printf("Time spent on primary needs%n  young people - %.2f%n  active people - %.2f%n  elder people - %.2f%n%n",
    calculateTime(primaryNeedsColumns, m => { val a = m("teage").toInt; 15 <= a && a <= 22 }),
    calculateTime(primaryNeedsColumns, m => { val a = m("teage").toInt; 23 <= a && a <= 55 }),
    calculateTime(primaryNeedsColumns, m => { val a = m("teage").toInt; a > 55 }))
  printf("Leisure time of working people compared to unemployed people:%n  working people - %.2f%n  unemployed people - %.2f%n",
    calculateTime(leisureColumns, m => { val i = m("telfs").toInt; 1 <= i && i < 3 }), calculateTime(leisureColumns, _("telfs").toInt == 5))

  def findColumns(columns: List[String]): List[String] = headers.filter(h => columns.exists(cn => h.startsWith(cn)))

  def calculateTime(columns: List[String], p: Map[String, String] => Boolean): Double = {
    val times = rows.filter(r => p(r.values)).map(_.values.view.filterKeys(columns.contains).toMap.map(_._2.toDouble).sum)
    times.sum / times.length
  }
}
