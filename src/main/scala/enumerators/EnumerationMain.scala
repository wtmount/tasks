package enumerators

import TrafficLightEnumerator._

object EnumerationMain extends App {
  val a = Adult("John", 50)
  val t = Teenager("Jack", 10)
  a.crossRoad(Yellow)
  t.crossRoad(Yellow)
  a.crossRoad(Y)
  t.crossRoad(Y)
}
