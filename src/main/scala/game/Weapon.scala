package game

trait Weapon {
  val damage: Int
  var durability: Int
}

case class LongSword() extends Weapon {
  override val damage: Int = 3
  override var durability: Int = 20
}

case class Dagger() extends Weapon {
  override val damage: Int = 2
  override var durability: Int = 15
}

case class Gladius() extends Weapon {
  override val damage: Int = 6
  override var durability: Int = 10
}