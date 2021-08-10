package binarytree

object Main extends App {
  val b: BinaryTree[Int, String] = new BinaryTree()
  val bt: BinaryTree[Int, String] = new BinaryTree()
  val bt2 = bt.insert(5, "five")
    .insert(10, "ten")
    .insert(2, "two")
    .insert(-1, "minus one")
    .insert(3, "three")
  bt2.printInorder()
  println()
  val bt3 = bt2.update(-1, "-one")
  bt3.printInorder()
  println()
  println(bt2.get(10))
  println(bt2.getOrEle(15, "fifteen"))
}
