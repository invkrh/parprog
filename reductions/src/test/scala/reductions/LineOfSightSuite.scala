package reductions

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory

@RunWith(classOf[JUnitRunner])
class LineOfSightSuite extends FunSuite {

  import LineOfSight._

  test("lineOfSight should correctly handle an array of size 4") {
    val output = new Array[Float](4)
    lineOfSight(Array[Float](0f, 1f, 8f, 9f), output)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }


  test("upsweepSequential should correctly handle the chunk 1 until 4 of an array of 4 elements") {
    val res = upsweepSequential(Array[Float](0f, 1f, 8f, 9f), 1, 4)
    assert(res == 4f)
  }


  test("downsweepSequential should correctly handle a 4 element array when the starting angle is zero") {
    val output = new Array[Float](4)
    downsweepSequential(Array[Float](0f, 1f, 8f, 9f), output, 0f, 1, 4)
    assert(output.toList == List(0f, 1f, 4f, 4f))
  }

  test("lineOfSightSeq and parLineOfSight should have the same result") {
    val scale = 10
    val output1 = new Array[Float](scale)
    val output2 = new Array[Float](scale)
    val input = Array.tabulate(scale)(_ => scala.util.Random.nextInt(scale).toFloat)
    lineOfSight(input, output1)
    parLineOfSight(input, output2, 4)
    assert(output1.toList == output2.toList)
  }

  test("upsweep should correctly compute the tree on the indices 1 until 5 of a 5 element array for threshold 1") {
    val input = Array(0F, 1F, 2F, 3F, 4F, 5F)
    val result = upsweep(input, 1, 5, 1)
    val expected = Node(Node(Leaf(1, 2, 1), Leaf(2, 3, 1)), Node(Leaf(3, 4, 1), Leaf(4, 5, 1)))
    assert(result == expected)
  }

}

