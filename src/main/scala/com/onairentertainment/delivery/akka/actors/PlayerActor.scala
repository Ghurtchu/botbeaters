package com.onairentertainment.delivery.akka.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.onairentertainment.core.model.Player
import com.onairentertainment.core.service.protocol.RandomNumberGenerator
import com.onairentertainment.delivery.akka.actors.PlayerActor.{Play, PlayerReply}
import com.onairentertainment.delivery.akka.actors.RandomNumberGeneratorActor.{GenerateRandomNumber, UpdatePlayer}

class PlayerActor (randomNumberGenerator: RandomNumberGenerator) extends Actor with ActorLogging {

  override def receive: Receive = withState(null)

  private final def withState(originalSender: ActorRef): Receive = {
    case Play(player) => {
      context.become(withState(sender()))
      val numberGeneratorActor = context.actorOf(Props(new RandomNumberGeneratorActor()))
      numberGeneratorActor ! GenerateRandomNumber(player)
    }
    case UpdatePlayer(player) => originalSender ! PlayerReply(player)
  }
}

object PlayerActor {

  final case class Play(player: Player)
  final case class PlayerReply(player: Player)

  def props(randomNumberGenerator: RandomNumberGenerator): Props =
    Props(new PlayerActor(randomNumberGenerator))

}
