package com.onairentertainment.solution.akka.actors

import akka.actor.{Actor, Props}
import com.onairentertainment.core.model.{Player, RandomNumber}
import com.onairentertainment.core.service.protocol.RandomNumberGenerator
import com.onairentertainment.solution.akka.actors.PlayerActor.{Play, PlayerReply}

class PlayerActor (randomNumberGenerator: RandomNumberGenerator) extends Actor {

  var randomNumber: Option[RandomNumber] = None

  override def receive: Receive = {
    case Play(player) =>
      randomNumber = Some(randomNumberGenerator.generate)
      val newPlayer = player.copy(randomNumber = this.randomNumber)
      sender ! PlayerReply(newPlayer)
  }
}

object PlayerActor {

  final case class Play(player: Player)
  final case class PlayerReply(player: Player)

  def props(randomNumberGenerator: RandomNumberGenerator): Props =
    Props(new PlayerActor(randomNumberGenerator))

}
