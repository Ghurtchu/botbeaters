package com.onairentertainment.solution.akka.actors

import akka.actor.{Actor, ActorLogging, Props}
import GameActor._
import com.onairentertainment.core.model.Player
import GameResultAggregatorActor._
import com.onairentertainment.core.service.implementation.{BoundedRandomNumberGenerator, OnAirResultAggregator, OnAirResultCalculator}
import com.onairentertainment.solution.akka.actors.PlayerActor.{Play, PlayerReply}

class GameActor extends Actor with ActorLogging {

  override def receive: Receive = withPlayers(0, List.empty)

  private final def withPlayers(numberOfPlayers: Int, playersWithRandomNumbers: List[Player]): Receive = {

    case InitializePlayers(nOfPlayers) =>
      log.info(s"Initializing the game for $nOfPlayers players")
      val players = for (_ <- 0 to nOfPlayers) yield Player()
      val playerActorRefs = for (player <- players) yield context.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 10_000)), s"player${player.id}")
      context.become(withPlayers(nOfPlayers, playersWithRandomNumbers))
      playerActorRefs.zip(players).foreach { pair =>
        val playerActorRef = pair._1
        val player = pair._2

        playerActorRef ! Play(player)
      }

    case PlayerReply(player) =>
      if (isLastPlayer(numberOfPlayers)) {
        val gameResultAggregatorActorRef = context.actorOf(GameResultAggregatorActor.props(new OnAirResultAggregator(new OnAirResultCalculator())))
        gameResultAggregatorActorRef ! AggregateResults(playersWithRandomNumbers)
      } else context.become(withPlayers(numberOfPlayers - 1, player :: playersWithRandomNumbers))

    case AggregatorReply(results) => log.info(results.mkString(start = "\n", sep = "\n", end = ""))

  }

  private final def isLastPlayer(numberOfPlayers: Int): Boolean = numberOfPlayers == 0
}


object GameActor {
  final case class InitializePlayers(numberOfPlayers: Int)
}
