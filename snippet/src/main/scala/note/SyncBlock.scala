package note

object SyncBlock extends App {

  private val x = new AnyRef

  private var uidCount = 0L

  def getUniqueUid: Long = x.synchronized {
    uidCount = uidCount + 1
    uidCount
  }

  //  def startThread() = {
  //    val t = new Thread {
  //      override def run(): Unit = {
  //        val uids = for (i <- 0 until 10) yield getUniqueID()
  //        println(uids)
  //      }
  //    }
  //    t.start()
  //    t
  //  }

  //  startThread(); startThread()

  class Account(private var amount: Int = 0, name: String) {

    val uid = getUniqueUid
    private def lockAndTransfer(target: Account, n: Int) = {
      println(name)
      this.synchronized {
        target.synchronized {
          this.amount -= n
          target.amount += n
        }
      }
    }

    def transfer(target: Account, n: Int) =
      if (this.uid < target.uid) this.lockAndTransfer(target, n)
      else target.lockAndTransfer(this, -n)

    //    def transfer(target: Account, n: Int) =
    //      this.synchronized {
    //        target.synchronized {
    //          this.amount -= n
    //          target.amount += n
    //        }
    //      }
  }

  def startThread(a: Account, b: Account, n: Int) = {
    val t = new Thread {
      override def run(): Unit = {
        for (i <- 0 until n) {
          a.transfer(b, 1)
        }
      }
    }
    t.start()
    t
  }

  val a1 = new Account(50000, "one")
  val a2 = new Account(70000, "two")

  val t = startThread(a1, a2, 10000)
  val s = startThread(a2, a1, 10000)

  t.join()
  s.join()


}
