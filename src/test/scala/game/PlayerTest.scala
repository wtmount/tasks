package game

import org.scalatest.flatspec.AnyFlatSpec

class PlayerTest extends AnyFlatSpec {
  behavior of "Player"

  it should "do nothing when player1 doesn't have weapon" in {
    val player1 = new Player("player1", 10, None, Some(SmallShield()))
    val player2 = new Player("player2", 10, Some(LongSword()), Some(BucklerShield()))
    player1.attack(player2)
    assert(player2.hp == 10)
    assert(player2.shield.get.durability == 12)
  }

  it should "reduce other player's hp and this player weapon's durability when other player doesn't have shield" in {
    val player1 = new Player("player1", 10, Some(LongSword()), Some(SmallShield()))
    val player2 = new Player("player2", 10, Some(Dagger()), None)
    player1.attack(player2)
    assert(player2.hp == 7)
    assert(player1.weapon.get.durability == 19)
  }

  it should "reduce other player shield and this player weapon's durability when shield is tougher" in {
    val player1 = new Player("player1", 10, Some(LongSword()), Some(SmallShield()))
    val player2 = new Player("player2", 10, Some(Dagger()), Some(Scutum()))
    player1.attack(player2)
    assert(player2.hp == 10)
    assert(player1.weapon.get.durability == 19)
    assert(player2.shield.get.durability == 14)
  }

  it should "reduce other player's hp, other player shield and this player weapon's durability when weapon is tougher" in {
    val player1 = new Player("player1", 10, Some(Gladius()), Some(SmallShield()))
    val player2 = new Player("player2", 10, Some(Dagger()), Some(SmallShield()))
    player1.attack(player2)
    assert(player2.hp == 9)
    assert(player1.weapon.get.durability == 9)
    assert(player2.shield.get.durability == 15)
  }
}
