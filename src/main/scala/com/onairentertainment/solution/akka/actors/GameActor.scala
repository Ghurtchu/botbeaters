package com.onairentertainment.solution.akka.actors

import akka.actor.{Actor, ActorLogging, Props}
import GameActor._
import com.onairentertainment.core.model.Player
import GameResultAggregatorActor._
import com.onairentertainment.core.service.implementation.{BoundedRandomNumberGenerator, OnAirResultAggregator, OnAirResultCalculator}
import com.onairentertainment.solution.akka.actors.PlayerActor.{Play, PlayerReply}

import scala.collection.mutable

class GameActor extends Actor with ActorLogging {

  var nPlayers: Int = 0 // initialize as 0
  val playersWithRandomNumbers: mutable.ArrayBuffer[Player] = mutable.ArrayBuffer.empty

  override def receive: Receive = {

    case InitializePlayers(numberOfPlayers) => {
      log.info(s"Initializing the game for $numberOfPlayers players")
      nPlayers = numberOfPlayers
      val players = for (_ <- 0 to numberOfPlayers) yield Player()
      val playerActorRefs = for (player <- players) yield context.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 10_000)), s"player${player.id}")
      playerActorRefs.zip(players).foreach { pair =>
        val playerActorRef = pair._1
        val player = pair._2

        playerActorRef ! Play(player)
      }
    }

    case PlayerReply(player) =>
      if (nPlayers == 0) {
        val gameResultAggregatorActorRef = context.actorOf(GameResultAggregatorActor.props(new OnAirResultAggregator(new OnAirResultCalculator())))
        gameResultAggregatorActorRef ! AggregateResults(playersWithRandomNumbers)
      } else {
        playersWithRandomNumbers.addOne(player)
        nPlayers -= 1
      }


    case AggregatorReply(results) => log.info(results.mkString(start = "\n", sep = "\n", end = ""))

  }
}

object GameActor {
  final case class InitializePlayers(numberOfPlayers: Int)
}
