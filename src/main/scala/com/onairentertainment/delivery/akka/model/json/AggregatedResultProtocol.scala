package com.onairentertainment.delivery.akka.model.json

import com.onairentertainment.core.domain.AggregatedResult
import spray.json.DefaultJsonProtocol

trait AggregatedResultProtocol extends DefaultJsonProtocol {
  implicit val aggregatedResultDefaultProtocol = jsonFormat4(AggregatedResult.apply)
}
