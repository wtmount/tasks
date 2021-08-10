package game

class Player(val name: String, var hp: Int, var weapon: Option[Weapon], var shield: Option[Shield]) {
  def attack(that: Player): Unit = {
    weapon match {
      case Some(weapon) =>
        if (weapon.durability > 1) weapon.durability -= 1 else this.weapon = None
        that.shield match {
          case Some(shield) =>
            if (shield.durability > 1) shield.durability -= 1 else that.shield = None
            val damage = weapon.damage - shield.defence
            if (damage > 0) doDamage(that, damage)
          case None =>
            doDamage(that, weapon.damage)
        }
      case None =>
    }
  }

  private def doDamage(player: Player, damage: Int): Unit = {
    player.hp -= damage
    isKilled(player)
  }

  private[game] def isKilled(player: Player): Unit = {
    if (player.hp <= 0) println(s"I beat ${player.name}")
  }
}
