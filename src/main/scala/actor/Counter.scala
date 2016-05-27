package actor

import java.time.temporal.ChronoUnit

import akka.actor.{Actor, ActorLogging}

/**
  * Created by victor on 27/05/16.
  */

class Counter extends Actor with ActorLogging{
  var total = 0L
  var counter = 0L

  var last: java.time.ZonedDateTime = java.time.ZonedDateTime.now

  def receive: Receive = {
    case Inc =>
      counter += 1
    case Show =>
      val now = java.time.ZonedDateTime.now
      val elapsed = ChronoUnit.SECONDS.between(last, now)
      last = now
      total += counter
      log.info("Total: {}, messages since last: {}, throughput: {} m/s", total, counter, counter / elapsed)
      counter = 0
    case Shutdown =>
      val now = java.time.ZonedDateTime.now
      val elapsed = ChronoUnit.SECONDS.between(last, now)
      total += counter
      log.info("Before shutdown: \n Total: {}, messages since last: {}, throughput: {} m/s", total, counter, counter / elapsed)
      context.system.terminate
      ()
  }
}
