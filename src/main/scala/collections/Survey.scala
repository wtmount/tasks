package collections

import collections.Activity.{Activity, All, Leisure, PrimaryNeeds, Work}

import scala.io.Source

object Survey extends App {
  val lines = Source.fromResource("atussum.csv").getLines.take(10000)
  val headers = lines.next.split(",").map(_.trim).toList

  val primaryNeedsColumns = findColumns(List("t01", "t03", "t11", "t1801", "t1803"))
  val workingColumns = findColumns(List("t05", "t1805"))
  val leisureColumns = findColumns(List("t02", "t04", "t06", "t07", "t08", "t09", "t10", "t12", "t13", "t14", "t15", "t16", "t18"))
    .filterNot(i => primaryNeedsColumns.contains(i) || workingColumns.contains(i))

  val respondents = lines.toList.map(_.split(",")).map(row => (headers zip row).toMap).map(entry => {
    val primaryNeeds = entry.filter(cn => primaryNeedsColumns.contains(cn._1)).map(_._2.toDouble).sum
    val work = entry.filter(cn => workingColumns.contains(cn._1)).map(_._2.toDouble).sum
    val leisure = entry.filter(cn => leisureColumns.contains(cn._1)).map(_._2.toDouble).sum
    Respondent(entry("tesex").toInt, entry("teage").toInt, entry("telfs").toInt, primaryNeeds, work, leisure)
  })

  printf("Average time spent on primary needs compared to leisure time%n  primary needs - %.2f%n  leisure time - %.2f%n%n",
    calculateTime(_ => true, PrimaryNeeds), calculateTime(_ => true, Leisure))
  printf("Women spend %.2f hours on work while men spend %.2f hours on work%n%n",
      calculateTime(_.sex != 1, Work), calculateTime(_.sex == 1, Work))
  printf("Time spent on primary needs%n  young people - %.2f%n  active people - %.2f%n  elder people - %.2f%n%n",
    calculateTime(r => 15 <= r.age && r.age <= 22, PrimaryNeeds), calculateTime(r => 23 <= r.age && r.age <= 55, PrimaryNeeds), calculateTime(_.age > 55, PrimaryNeeds))
  printf("Leisure time of working people compared to unemployed people:%n  working people - %.2f%n  unemployed people - %.2f%n",
    calculateTime(r => 1 <= r.workStatus && r.workStatus < 3, Leisure), calculateTime(_.workStatus == 5, Leisure))

  def findColumns(columns: List[String]): List[String] = headers.filter(h => columns.exists(cn => h.startsWith(cn)))

  def calculateTime(p: Respondent => Boolean, activity: Activity): Double = {
    val res = respondents.filter(p)
    val totalTime = activity match {
      case PrimaryNeeds => res.map(_.primaryNeeds)
      case Work => res.map(_.work)
      case Leisure => res.map(_.leisure)
      case All => res.map(r => r.primaryNeeds + r.work + r.leisure)
    }
    totalTime.sum / res.length
  }
}

case class Respondent(sex: Int, age: Int, workStatus: Int, primaryNeeds: Double, work: Double, leisure: Double)

object Activity extends Enumeration {
  type Activity = Value
  val PrimaryNeeds, Work, Leisure, All = Value
}