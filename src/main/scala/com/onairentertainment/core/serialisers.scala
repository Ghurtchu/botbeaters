package com.onairentertainment.core

import com.onairentertainment.core.domain.{AggregatedResult, PlayPayload}
import com.onairentertainment.delivery.akka.actors.ping.PingPongActor.{Ping, Pong}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object serialisers {

  type Json[A] = RootJsonFormat[A]

  trait AggregatedResultProtocol extends DefaultJsonProtocol {
    implicit val aggregatedResultDefaultProtocol: Json[AggregatedResult] =
      jsonFormat4(AggregatedResult.apply)
  }

  trait PingJsonProtocol extends DefaultJsonProtocol {
    final implicit val pingFormat: Json[Ping] =
      jsonFormat3(Ping)
  }

  trait PlayPayloadJsonProtocol extends DefaultJsonProtocol {
    implicit val playPayloadFormat: Json[PlayPayload] =
      jsonFormat2(PlayPayload)
  }

  trait PongJsonProtocol extends DefaultJsonProtocol {
    implicit val pongFormat: Json[Pong] =
      jsonFormat4(Pong)
  }

}
