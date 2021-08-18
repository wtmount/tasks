package enumerators

import TrafficLightEnumerator._

trait Person {
  val name: String
  val age: Int
  val yellowMessage: String
  val greenMessage = "Cross the road"
  val redMessage = "Stop"

  def crossRoad(light: TrafficLightEnumerator): Unit = println(getMessage(light))
  def crossRoad(light: TrafficLight): Unit = println(getMessage(light))

  private def getMessage(light: TrafficLightEnumerator): String =
    light match {
      case Green => greenMessage
      case Red => redMessage
      case Yellow => yellowMessage
    }

  private def getMessage(light: TrafficLight): String =
    light match {
      case G => greenMessage
      case R => redMessage
      case Y => yellowMessage
    }
}

case class Adult(name: String, age: Int) extends Person {
  override val yellowMessage: String = "Stop walking"
}

case class Teenager(name: String, age: Int) extends Person {
  override val yellowMessage: String = "Start running"
}