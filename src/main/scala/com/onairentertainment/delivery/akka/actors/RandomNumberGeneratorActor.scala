package com.onairentertainment.delivery.akka.actors

import akka.actor.{Actor, ActorLogging}
import com.onairentertainment.core.model.{Player, RandomNumber}
import com.onairentertainment.core.service.implementation.BoundedRandomNumberGenerator
import com.onairentertainment.core.service.protocol.RandomNumberGenerator

class RandomNumberGeneratorActor extends Actor with ActorLogging {

  import RandomNumberGeneratorActor._

  private final lazy val randomNumberGenerator: RandomNumberGenerator =
    new BoundedRandomNumberGenerator(from = 0, to = 999_999)

  override def receive: Receive = {
    case GenerateRandomNumber(player) =>
      log.info(s"Generating random number for ${player.id}")
      val randomNumber = randomNumberGenerator.generate
      sender() ! UpdatePlayer(player.copy(randomNumber = Some(randomNumber)))
  }
}

object RandomNumberGeneratorActor {
  final case class GenerateRandomNumber(player: Player)
  final case class UpdatePlayer(player: Player)
}
