package binarytree

import scala.Ordering.Implicits._

class BinaryTree[K : Ordering, +V](root: Option[Node[K, V]] = None) {
  def get(key: K): Option[V] = find(key, root) match {
    case Some(node) => Some(node.value)
    case None => None
  }

  def getOrEle[A >: V](key: K, default: => A): A = find(key, root) match {
    case Some(node) => node.value
    case None => default
  }

  def insert[A >: V](newKey: K, newValue: A): Option[BinaryTree[K, A]] = find(newKey, root) match {
    case Some(n) => None
    case None => Some(new BinaryTree(insertNode(newKey, newValue, root)))
  }

  def update[A >: V](key: K, newValue: A): BinaryTree[K, A] = new BinaryTree(updateNode(key, newValue, root))

  def printInorder(): Unit = printNode(root)

  private def find[A >: V](findKey: K, currentNode: Option[Node[K, A]]): Option[Node[K, A]] = {
    currentNode match {
      case None => None
      case Some(n) if findKey < n.key => find(findKey, n.left)
      case Some(n) if findKey > n.key => find(findKey, n.right)
      case _ => currentNode
    }
  }

  private def insertNode[A >: V](newKey: K, newValue: A, node: Option[Node[K, A]]): Option[Node[K, A]] = {
    node match {
      case None => Some(Node(None, None, newKey, newValue))
      case Some(n) if newKey < n.key => Some(n.copy(left = insertNode(newKey, newValue, n.left)))
      case Some(n) if newKey > n.key => Some(n.copy(right = insertNode(newKey, newValue, n.right)))
    }
  }

  private def updateNode[A >: V](key: K, newValue: A, node: Option[Node[K, A]]): Option[Node[K, A]] = {
    node match {
      case None => None
      case Some(n) if key < n.key => Some(n.copy(left = updateNode(key, newValue, n.left)))
      case Some(n) if key > n.key => Some(n.copy(right = updateNode(key, newValue, n.right)))
      case Some(n) => Some(n.copy(value = newValue))
    }
  }

  private def printNode[A >: V](node: Option[Node[K, A]]): Unit = node.foreach(n => { printNode(n.left); print(s"${n.key}->${n.value}  "); printNode(n.right) })
}

case class Node[K, +V](left: Option[Node[K, V]], right: Option[Node[K, V]], key: K, value: V)