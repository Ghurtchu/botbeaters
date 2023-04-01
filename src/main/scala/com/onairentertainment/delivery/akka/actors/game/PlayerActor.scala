package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.implementation.BoundedRandomNumberGenerator
import com.onairentertainment.core.service.protocol.RandomNumberGenerator
import com.onairentertainment.delivery.akka.actors.game.PlayerActor.{Play, PlayerReply}
import com.onairentertainment.delivery.akka.actors.game.RandomNumberGeneratorActor.{GenerateRandomNumber, PlayerUpdated}

final class PlayerActor (randomNumberGenerator: RandomNumberGenerator) extends Actor with ActorLogging {

  override def receive: Receive = withState(null)

  private final def withState(originalSender: ActorRef): Receive = {
    case Play(player) => {
      context.become(withState(sender()))
      val numberGeneratorActor = context.actorOf(RandomNumberGeneratorActor.props(new BoundedRandomNumberGenerator(from = 0, to = 999_999)))
      numberGeneratorActor ! GenerateRandomNumber(player)
    }
    case PlayerUpdated(player) => originalSender ! PlayerReply(player)
  }
}

object PlayerActor {

  final case class Play(player: Player)
  final case class PlayerReply(player: Player)

  def props(randomNumberGenerator: RandomNumberGenerator): Props =
    Props(new PlayerActor(randomNumberGenerator))

}
