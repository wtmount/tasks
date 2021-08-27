package collections

import collections.Activity.{Activity, All, Leisure, PrimaryNeeds, Work}
import collections.Age.{Age, Active, Elder, Young}
import collections.Employability.{Employability, Unemployed, Working}
import collections.Sex.{Sex, Female, Male}

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
      calculateTime(_.sex == Female, Work), calculateTime(_.sex == Male, Work))
  printf("Time spent on primary needs%n  young people - %.2f%n  active people - %.2f%n  elder people - %.2f%n%n",
    calculateTime(_.age == Young, PrimaryNeeds), calculateTime(_.age == Active, PrimaryNeeds), calculateTime(_.age == Elder, PrimaryNeeds))
  printf("Leisure time of working people compared to unemployed people:%n  working people - %.2f%n  unemployed people - %.2f%n",
    calculateTime(_.workStatus == Working, Leisure), calculateTime(_.workStatus == Unemployed, Leisure))

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

case class Respondent(sex: Sex, age: Age, workStatus: Employability, primaryNeeds: Double, work: Double, leisure: Double)

object Respondent {
  def apply(sex: Int, age: Int, workStatus: Int, primaryNeeds: Double, work: Double, leisure: Double): Respondent = {
    val sexEnum = if (sex == 1) Male else Female
    val ageEnum =
      if (15 <= age && age <= 22) Young
      else if (23 <= age && age <= 55) Active
      else Elder
    val workEnum = if (1 <= workStatus && workStatus < 3) Working else Unemployed
    new Respondent(sexEnum, ageEnum, workEnum, primaryNeeds, work, leisure)
  }
}

object Activity extends Enumeration {
  type Activity = Value
  val PrimaryNeeds, Work, Leisure, All = Value
}

object Sex extends Enumeration {
  type Sex = Value
  val Male, Female = Value
}

object Age extends Enumeration {
  type Age = Value
  val Young, Active, Elder = Value
}

object Employability extends Enumeration {
  type Employability = Value
  val Working, Unemployed = Value
}