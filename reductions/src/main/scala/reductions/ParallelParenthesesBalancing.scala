package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer (new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def balance(chars: Array[Char]): Boolean = {
    def fold(text: Array[Char], acc: Int): Int = {
      if (acc < 0) acc
      else {
        text match {
          case Array() => acc
          case Array(x, xs@_*) =>
            if (x == '(') fold(xs.toArray, acc + 1)
            else if (x == ')') fold(xs.toArray, acc - 1)
            else fold(xs.toArray, acc)
        }
      }
    }
    fold(chars, 0) == 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(from: Int, until: Int, acc: Int, minima: Int): (Int, Int) = {
      if (from < until) {
        if (chars(from) == '(') traverse(from + 1, until, acc + 1, minima)
        else if (chars(from) == ')') traverse(from + 1, until, acc - 1, scala.math.min(minima, acc - 1))
        else traverse(from + 1, until, acc, minima)
      } else {
        (acc, minima)
      }
    }

    def reduce(from: Int, until: Int): (Int, Int) = {
      if (until - from <= threshold) {
        traverse(from, until, 0, 0)
      } else {
        val mid = from + (until - from) / 2
        val ((accLeft, minLeft), (accRight, minRight)) = parallel(reduce(from, mid), reduce(mid, until))
        (accLeft + accRight, scala.math.min(minLeft, accLeft + minRight))
      }
    }

    reduce(0, chars.length) == (0, 0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
