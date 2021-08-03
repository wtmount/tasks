package game

trait Shield {
  val defence: Int
  var durability: Int
}

case class SmallShield() extends Shield {
  override val defence: Int = 5
  override var durability: Int = 16
}

case class BucklerShield() extends Shield {
  override val defence: Int = 3
  override var durability: Int = 12
}

case class Scutum() extends Shield {
  override val defence: Int = 6
  override var durability: Int = 15
}