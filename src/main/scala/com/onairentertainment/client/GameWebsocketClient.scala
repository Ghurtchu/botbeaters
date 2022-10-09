package com.onairentertainment.client

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage, WebSocketRequest}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.onairentertainment.delivery.akka.model.PlayPayload
import com.onairentertainment.delivery.akka.model.json.{AggregatedResultProtocol, PlayPayloadJsonProtocol}
import spray.json._

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object GameWebsocketClient extends scala.App
  with PlayPayloadJsonProtocol
  with AggregatedResultProtocol
  with SprayJsonSupport {

  println("initializing WebSocketClient")

  implicit val system = ActorSystem("GameWebsocketClientSystem")

  val playPayloadSource: Source[Message, NotUsed] =
    Source.repeat {
      val players = scala.util.Random.nextInt(10)
      val playPayload = PlayPayload("request.play", players)
      val json = playPayload.toJson.prettyPrint
      TextMessage(json)
    }.delay(3.seconds)

  val printSink: Sink[Message, Future[Done]] = Sink.foreach {
    case TextMessage.Strict(msg) =>
      println("-" * 25 concat ("-" * 25))
      println(msg)
    case _ => println("We don't care")
  }

  val flow: Flow[Message, Message, Future[Done]] = Flow.fromSinkAndSourceMat(printSink, playPayloadSource)(Keep.left)

  Http().singleWebSocketRequest(WebSocketRequest("ws://localhost:8080/play"), flow)

}
