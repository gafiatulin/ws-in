package actor

import java.time.temporal.ChronoUnit

import akka.actor.{Actor, ActorLogging}

/**
  * Created by victor on 27/05/16.
  */

class Counter extends Actor with ActorLogging{
  var total = 0L
  var count = 0L

  var last: java.time.ZonedDateTime = java.time.ZonedDateTime.now

  def receive: Receive = {
    case Inc =>
      count += 1
    case Show =>
      val now = java.time.ZonedDateTime.now
      val elapsed = ChronoUnit.SECONDS.between(last, now)
      last = now
      total += count
      log.info("Total: {}, messages since last: {}, throughput: {} m/s", total, count, count / elapsed)
      count = 0
  }
}
