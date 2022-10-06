package com.onairentertainment.delivery.akka.actors

import akka.actor.{Actor, ActorRef, Props}
import com.onairentertainment.core.model.{Player, RandomNumber}
import com.onairentertainment.core.service.protocol.RandomNumberGenerator
import com.onairentertainment.delivery.akka.actors.PlayerActor.{Play, PlayerReply}

class PlayerActor (randomNumberGenerator: RandomNumberGenerator) extends Actor {

  override def receive: Receive = {
    case Play(player, originalSender) =>
      val randNumber = Some(randomNumberGenerator.generate)
      val playerWithRandNumber = player.copy(randomNumber = randNumber)
      sender ! PlayerReply(playerWithRandNumber, originalSender)
  }
}

object PlayerActor {

  final case class Play(player: Player, originalSender: ActorRef)
  final case class PlayerReply(player: Player, originalSender: ActorRef)

  def props(randomNumberGenerator: RandomNumberGenerator): Props =
    Props(new PlayerActor(randomNumberGenerator))

}
