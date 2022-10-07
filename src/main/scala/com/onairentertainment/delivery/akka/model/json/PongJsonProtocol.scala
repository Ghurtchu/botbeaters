package com.onairentertainment.delivery.akka.model.json

import com.onairentertainment.delivery.akka.model.{PlayPayload, Pong}
import spray.json.DefaultJsonProtocol

trait PongJsonProtocol extends DefaultJsonProtocol {
  implicit val pongFormat = jsonFormat4(Pong)
}
