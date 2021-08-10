package recursion

import scala.annotation.tailrec

class Retry(stopper: Stopper) {

  @tailrec
  final def retry[T](block: () => T, accept: T => Boolean, retries: List[Long]): Option[T] = {
    block() match {
      case res if accept(res) => Some(res)
      case _ if retries.isEmpty => None
      case _ =>
        stopper.pause(retries.head)
        retry(block, accept, retries.tail)
    }
  }
}

class Stopper {
  def pause(ms: Long): Unit = Thread.sleep(ms)
}