package monad

import scala.io.StdIn.readLine

trait Monad[M[_]] {
  def unit[A](a: A): M[A]
  def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]
}

object Monad {
  def unit[A, M[_] : Monad](a: A): M[A] = implicitly[Monad[M]].unit(a)

  implicit class MonadOps[A, M[_] : Monad](m: M[A])  {
    def flatMap[B](f: A => M[B]): M[B] = implicitly[Monad[M]].flatMap(m)(f)
  }
}

class IO[A](a: => A) {
  def run: A = a
  def map[B](f: A => B): IO[B] = new IO(f(run))
  def flatMap[B](f: A => IO[B]): IO[B] = new IO(f(run).run)
}

object IOMonad extends Monad[IO] {
  override def unit[A](a: A): IO[A] = new IO[A](a)
  override def flatMap[A, B](m: IO[A])(f: A => IO[B]): IO[B] = m.flatMap(f)

  def output(s: String): IO[Unit] = new IO[Unit](println(s))
  def input(): IO[String] = new IO[String](readLine())
}

object IOApp extends App {
  val a = for {
    _ <- IOMonad.output("enter a string")
    s <- IOMonad.input()
    _ <- IOMonad.output(s)
  } yield s

  a.run
}