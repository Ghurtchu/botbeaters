package com.onairentertainment.delivery.akka.model.json

import com.onairentertainment.delivery.akka.model.{Ping}
import spray.json.DefaultJsonProtocol

trait PingJsonProtocol extends DefaultJsonProtocol {
  implicit val pingFormat = jsonFormat3(Ping)
}
