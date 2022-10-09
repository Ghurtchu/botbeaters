package com.onairentertainment.delivery.akka.model.json

import com.onairentertainment.delivery.akka.actors.ping.PingPongActor.Ping
import spray.json.DefaultJsonProtocol

trait PingJsonProtocol extends DefaultJsonProtocol {
  final implicit val pingFormat = jsonFormat3(Ping)
}
