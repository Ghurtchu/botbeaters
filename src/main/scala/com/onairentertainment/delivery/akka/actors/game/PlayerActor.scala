package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.implementation.BoundedRNG
import com.onairentertainment.core.service.protocol.RNG
import com.onairentertainment.delivery.akka.actors.game.PlayerActor.{Play, PlayerReply}
import com.onairentertainment.delivery.akka.actors.game.RNGActor.{GenerateRandomNumber, PlayerUpdated}

final class PlayerActor (rng: RNG) extends Actor with ActorLogging {

  override def receive: Receive = withState(None)

  private def withState(originalSender: Option[ActorRef]): Receive = {

    case Play(player) => {
      context.become(withState(Some(sender())))
      val rngActor = context.actorOf(RNGActor.props(rng))

      rngActor ! GenerateRandomNumber(player)
    }

    case PlayerUpdated(player) =>
      originalSender.foreach(_ ! PlayerReply(player))

  }

}

object PlayerActor {

  final case class Play(player: Player)
  final case class PlayerReply(player: Player)

  def props(rng: RNG): Props =
    Props(new PlayerActor(rng))

}
