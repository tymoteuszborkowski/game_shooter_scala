package pl.tymoteuszborkowski.utils

import java.util.{Timer, TimerTask}

class Delay(val amount: Long,
            val timer: Timer = new Timer(true)) {

  def execute(task: Runnable): Unit = {
    if (amount == 0) {
      task.run()
      return
    }
    timer.schedule(new TimerTask {
      override def run(): Unit = task.run()
    }, amount)
  }

}
