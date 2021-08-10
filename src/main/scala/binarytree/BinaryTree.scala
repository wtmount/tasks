package binarytree

import scala.Ordering.Implicits._

class BinaryTree[K : Ordering, V](root: Option[Node[K, V]] = None) {
  def get(key: K): V = find(key, root).map(_.value).getOrElse(throw new IllegalArgumentException("No such element in the tree"))
  def getOrEle(key: K, default: V): V = find(key, root).map(_.value).getOrElse(default)
  def insert(newKey: K, newValue: V): BinaryTree[K, V] = new BinaryTree(insertNode(newKey, newValue, root))
  def update(key: K, newValue: V): BinaryTree[K, V] = new BinaryTree(updateNode(key, newValue, root))
  def printInorder(): Unit = printNode(root)

  private def find(findKey: K, currentNode: Option[Node[K, V]]): Option[Node[K, V]] = {
    currentNode match {
      case None => None
      case Some(n) if findKey < n.key => find(findKey, n.left)
      case Some(n) if findKey > n.key => find(findKey, n.right)
      case _ => currentNode
    }
  }

  private def insertNode(newKey: K, newValue: V, node: Option[Node[K, V]]): Option[Node[K, V]] = {
    node match {
      case None => Some(Node(None, None, newKey, newValue))
      case Some(n) if newKey < n.key => Some(Node(insertNode(newKey, newValue, n.left), n.right, n.key, n.value))
      case Some(n) if newKey > n.key => Some(Node(n.left, insertNode(newKey, newValue, n.right), n.key, n.value))
      case _ => node
    }
  }

  private def updateNode(key: K, newValue: V, node: Option[Node[K, V]]): Option[Node[K, V]] = {
    node match {
      case None => throw new IllegalArgumentException("No such element in the tree")
      case Some(n) if key < n.key => Some(Node(updateNode(key, newValue, n.left), n.right, n.key, n.value))
      case Some(n) if key > n.key => Some(Node(n.left, updateNode(key, newValue, n.right), n.key, n.value))
      case Some(n) => Some(Node(n.left, n.right, n.key, newValue))
    }
  }

  private def printNode(node: Option[Node[K, V]]): Unit = node.foreach(n => { printNode(n.left); print(s"${n.key}->${n.value}  "); printNode(n.right) })
}

case class Node[K, V](left: Option[Node[K, V]], right: Option[Node[K, V]], key: K, value: V)