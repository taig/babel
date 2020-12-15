package io.taig.lokal

import cats.Functor

final case class Result[+A](rank: Rank, value: A) {
  def withRank(rank: Rank): Result[A] = Result(rank, value)
}

object Result {
  implicit val functor: Functor[Result] = new Functor[Result] {
    override def map[A, B](fa: Result[A])(f: A => B): Result[B] = Result(fa.rank, f(fa.value))
  }
}
