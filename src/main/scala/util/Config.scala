package util

import java.util.concurrent.TimeUnit

import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.FiniteDuration
import scala.util.Try

/**
  * Created by victor on 27/05/16.
  */
trait Config {
  private val config = ConfigFactory.load()

  private def finiteDuration(key: String) = (for{
    c <- Try(config.getConfig(key))
    v <- Try(c.getLong("val"))
    u <- Try(c.getString("unit"))
    d <- Try(FiniteDuration(v, TimeUnit.valueOf(u)))
  } yield d).getOrElse(throw new Exception(s"Wasn't able to parse Finite Duration for key $key"))

  val interface = config.getString("interface")

  val port = config.getInt("port")

  val defaultDuration = finiteDuration("defaultDuration")
}
