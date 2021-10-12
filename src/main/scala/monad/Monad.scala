package monad

import scala.io.StdIn.readLine

object IOApp extends App {
  trait Monad[M[_]] {
    def unit[A](a: A): M[A]
    def map[A, B](m: M[A])(f: A => B): M[B]
    def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]
  }

  object Monad {
    def unit[A, M[_] : Monad](a: A): M[A] = implicitly[Monad[M]].unit(a)
    implicit class MonadOps[A, M[_] : Monad](m: M[A]) {
      def map[B](f: A => B): M[B] = implicitly[Monad[M]].map(m)(f)
      def flatMap[B](f: A => M[B]): M[B] = implicitly[Monad[M]].flatMap(m)(f)
    }
  }

  class IO[A](a: => A) { def run: A = a }

  def output(line: String): IO[Unit] = new IO(println(line))
  def input(): IO[String] = new IO(readLine)

  implicit object IOMonad extends Monad[IO] {
    def unit[A](a: A): IO[A] = new IO(a)
    def map[A, B](m: IO[A])(f: A => B): IO[B] = new IO(f(m.run))
    def flatMap[A, B](m: IO[A])(f: A => IO[B]): IO[B] = new IO(f(m.run).run)
  }

  import Monad._

//  val line = output("Input line:") flatMap (_ => input()) flatMap output
//  line.run

  val line = for {
    _ <- output("Input line:")
    line <- input()
    _ <- output(line)
  } yield line
  line.run
}