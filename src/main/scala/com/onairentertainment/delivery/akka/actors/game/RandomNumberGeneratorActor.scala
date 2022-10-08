package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{Actor, ActorLogging, Props}
import com.onairentertainment.core.model.Player
import com.onairentertainment.core.service.protocol.RandomNumberGenerator

final class RandomNumberGeneratorActor(randomNumberGenerator: RandomNumberGenerator) extends Actor with ActorLogging {

  import RandomNumberGeneratorActor._

  override def receive: Receive = {
    case GenerateRandomNumber(player) =>
      log.info(s"Generating random number for ${player.id}")
      val randomNumber = randomNumberGenerator.generate
      sender() ! PlayerUpdated(player.copy(randomNumber = Some(randomNumber)))
  }
}

object RandomNumberGeneratorActor {

  def props(randomNumberGenerator: RandomNumberGenerator): Props =
    Props(new RandomNumberGeneratorActor(randomNumberGenerator))

  final case class GenerateRandomNumber(player: Player)
  final case class PlayerUpdated(player: Player)
}
