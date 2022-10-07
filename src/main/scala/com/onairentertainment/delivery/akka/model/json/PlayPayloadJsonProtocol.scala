package com.onairentertainment.delivery.akka.model.json

import com.onairentertainment.delivery.akka.model.PlayPayload
import spray.json.DefaultJsonProtocol

trait PlayPayloadJsonProtocol extends DefaultJsonProtocol {
  implicit val playPayloadFormat = jsonFormat2(PlayPayload)
}
