package binarytree

import org.scalatest.flatspec.AnyFlatSpec

import java.io.ByteArrayOutputStream

class BinaryTreeTest extends AnyFlatSpec {

  behavior of "BinaryTree"

  it should "return value from get when value is present" in {
    val bt = new BinaryTree[Int, String].insert(10, "ten")
    assert(bt.get.get(10).get == "ten")
  }

  it should "return None from get when value is not present" in {
    val bt = new BinaryTree[Int, String]
    assert(bt.get(10).isEmpty)
  }

  it should "return value from getOrElse when value is present" in {
    val bt = new BinaryTree[Int, String].insert(10, "ten")
    assert(bt.get.getOrEle(10, "X") == "ten")
  }

  it should "return default from getOrElse when value is not present" in {
    val bt = new BinaryTree[Int, String].insert(1, "one")
    assert(bt.get.getOrEle(10, "X") == "X")
  }

  it should "insert new value in a binary tree" in {
    val bt = new BinaryTree[Int, String].insert(1, "one")
    assert(bt.get.get(1).get == "one")
  }

  it should "fail when trying to insert duplicated value" in {
    val bt = new BinaryTree[Int, String].insert(1, "one").get.insert(1, "I")
    assert(bt.isEmpty)
  }

  it should "update existing value" in {
    val bt = new BinaryTree[Int, String].insert(10, "ten").get.update(10, "X")
    assert(bt.get(10).get == "X")
  }

  it should "not update anything if value not present" in {
    val bt = new BinaryTree[Int, String].insert(1, "one").get.update(10, "X")
    assert(bt.get(1).get == "one")
  }

  it should "print elements of tree inorder" in {
    val bt = new BinaryTree[Int, String]().insert(5, "five").get
        .insert(10, "ten").get
        .insert(2, "two").get
        .insert(-1, "minus one").get
        .insert(3, "three").get
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      bt.printInorder()
    }
    assert(output.toString == "-1->minus one  2->two  3->three  5->five  10->ten  ")
  }
}
