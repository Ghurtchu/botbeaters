package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{Actor, ActorLogging, Props}
import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.protocol.RNGService

final class RNGActor(randomNumberGenerator: RNGService) extends Actor with ActorLogging {

  import RNGActor._

  override def receive: Receive = {

    case GenerateRandomNumber(player) => {
      log.info(s"Generating random number for ${player.id}")
      val randomNumber = randomNumberGenerator.gen
      val playerUpdated = player.copy(randomNumber = Some(randomNumber))

      sender() ! PlayerUpdated(playerUpdated)
    }
  }

}

object RNGActor {

  def props(randomNumberGenerator: RNGService): Props =
    Props(new RNGActor(randomNumberGenerator))

  final case class GenerateRandomNumber(player: Player)
  final case class PlayerUpdated(player: Player)
}
