package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._

import ParallelParenthesesBalancing._

@RunWith(classOf[JUnitRunner])
class ParallelParenthesesBalancingSuite extends FunSuite {

  test("balance should work for empty string") {
    def check(input: String, expected: Boolean) =
      assert(balance(input.toArray) == expected,
        s"balance($input) should be $expected")

    check("", true)
  }

  test("balance should work for string of length 1") {
    def check(input: String, expected: Boolean) =
      assert(balance(input.toArray) == expected,
        s"balance($input) should be $expected")

    check("(", false)
    check(")", false)
    check(".", true)
  }

  test("balance should work for string of length 2") {
    def check(input: String, expected: Boolean) =
      assert(balance(input.toArray) == expected,
        s"balance($input) should be $expected")

    check("()", true)
    check(")(", false)
    check("((", false)
    check("))", false)
    check(".)", false)
    check(".(", false)
    check("(.", false)
    check(").", false)
  }

  test("parBalance should work") {
    def check(input: String, expected: Boolean) =
      assert(parBalance(input.toArray, 5) == expected,
        s"balance($input) should be $expected")

    check("(if (zero? x) max (/ 1 x))", true)
    check("I told him (that it's not (yet) done). (But he wasn't listening)", true)
    check("I told him (that it's not (yet) done). But he wasn't listening)", false)
    check("(o_()", false)
    check(":-)", false)
    check("())(", false)
  }

  test("reduction function") {
    import scala.util.Random._
    def rdIntPair = (nextInt(20), if (nextDouble < 0.5) -nextInt(20) else 0)
    val input = List.tabulate(100)(_ => rdIntPair)
    val res1 = input.foldLeft((0, 0)) {
      case ((accLeft, minLeft), (accRight, minRight)) =>
        (accLeft + accRight, scala.math.min(minLeft, accLeft + minRight))
    }
    val res2 = input.foldRight((0, 0)) {
      case ((accLeft, minLeft), (accRight, minRight)) =>
        (accLeft + accRight, scala.math.min(minLeft, accLeft + minRight))
    }

    assert(res1 == res2, s"reduction function is not associative")
  }
}