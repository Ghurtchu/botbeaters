package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{Actor, ActorLogging, Props}
import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.protocol.RNG

final class RNGActor(rng: RNG) extends Actor with ActorLogging {

  import RNGActor._

  override def receive: Receive = {

    case GenerateRandomNumber(player) => {
      log.info(s"Generating random number for ${player.id}")
      val playerUpdated = player.copy(randomNumber = Some(rng.gen))

      sender() ! PlayerUpdated(playerUpdated)
    }
  }

}

object RNGActor {

  def props(rng: RNG): Props =
    Props(new RNGActor(rng))

  final case class GenerateRandomNumber(player: Player)
  final case class PlayerUpdated(player: Player)
}
