package com.onairentertainment.client

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.onairentertainment.delivery.akka.actors.ping.PingPongActor.Ping
import com.onairentertainment.delivery.akka.model.json.PingJsonProtocol
import spray.json._

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object PingPongWebsocketClient extends scala.App with PingJsonProtocol {

  println("initializing WebSocketClient")

  implicit val system = ActorSystem("PingWebsocketClientSystem")

  def ping = Ping(id = scala.util.Random.nextInt(100), timestamp = System.currentTimeMillis(), messageType = "request.ping")

  val playPayloadSource: Source[Message, NotUsed] =
    Source.repeat(TextMessage(ping.toJson.prettyPrint)).delay(3.seconds)

  val printSink: Sink[Message, Future[Done]] = Sink.foreach {
    case TextMessage.Strict(msg) =>
      println("-" * 25 concat ("-" * 25))
      println(msg)
    case _ => println("We don't care")
  }

  val flow: Flow[Message, Message, Future[Done]] = Flow.fromSinkAndSourceMat(printSink, playPayloadSource)(Keep.left)

  Http().singleWebSocketRequest(WebSocketRequest("ws://localhost:8080/ping"), flow)

  println("initialized WebSocketClient successfully")

}