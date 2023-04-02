package com.onairentertainment.delivery.akka.actors.ping

import akka.actor.{Actor, ActorLogging}

final class PingPongActor extends Actor with ActorLogging {

  import PingPongActor._

  override def receive: Receive = {

    case Ping(id, msgType, timestamp) => {
      val now = System.currentTimeMillis()
      val pong = msgType.replace("ping", "pong")

      sender() ! Pong(id, pong, timestamp, now)
    }
  }
}

object PingPongActor {
  final case class Pong(requestId: Int, messageType: String, requestAt: Long, timestamp: Long)
  final case class Ping(id: Int, messageType: String, timestamp: Long)
}
