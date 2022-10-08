package com.onairentertainment.delivery.akka.actors.ping

import akka.actor.{Actor, ActorLogging}

class PingPongActor extends Actor with ActorLogging {

  import PingPongActor._

  override def receive: Receive = {
    case Ping(id, msgType, timestamp) => sender() ! Pong(id, msgType.replace("ping", "pong"), timestamp, System.currentTimeMillis())
  }
}

object PingPongActor {
  case class Pong(requestId: Int, messageType: String, requestAt: Long, timestamp: Long)
  case class Ping(id: Int, messageType: String, timestamp: Long)
}
