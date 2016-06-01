import java.util.concurrent.Executors

import actor.{Counter, Inc, Show, Shutdown}
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import util.Config

import scala.concurrent.ExecutionContext

/**
  * Created by victor on 26/05/16.
  */


object Main extends App with Config {
  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()
  implicit val ec = ExecutionContext.fromExecutor(Executors.newCachedThreadPool)

  val counter = system.actorOf(Props[Counter])

  system.scheduler.schedule(defaultDuration, defaultDuration, counter, Show)

  val sink = Sink.foreach[Message]{
    case tm: TextMessage.Strict => counter ! Inc
    case _ => ()
  }

  def wsHandler: Flow[Message, Message, Any] = Flow.fromSinkAndSource(sink, Source.maybe)

  val routes = handleWebSocketMessages(wsHandler) ~ complete{
    counter ! Inc
    StatusCodes.OK
  }

  Http().bindAndHandle(routes, interface, port)
}

