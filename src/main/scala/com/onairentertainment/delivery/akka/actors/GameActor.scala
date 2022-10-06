package com.onairentertainment.delivery.akka.actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import GameActor._
import com.onairentertainment.core.model.{AggregatedResult, Player}
import GameResultAggregatorActor._
import com.onairentertainment.core.service.implementation.{BoundedRandomNumberGenerator, OnAirResultAggregator, OnAirResultCalculator}
import com.onairentertainment.delivery.akka.actors.PlayerActor.{Play, PlayerReply}

class GameActor extends Actor with ActorLogging {

  private final lazy val originalSender: ActorRef = sender()

  override def receive: Receive = withState(0, List.empty)

  private final def withState(numberOfPlayers: Int, playersWithRandomNumbers: List[Player]): Receive = {

    case InitializePlayers(nOfPlayers) =>
      log.info(s"Initializing the game for $nOfPlayers players")
      val players = for (_ <- 1 to nOfPlayers) yield Player()
      val playerActorRefs = for (player <- players) yield context.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 10_000)), s"player${player.id}")
      context.become(withState(nOfPlayers, playersWithRandomNumbers))
      playerActorRefs.zip(players).foreach { pair =>
        val playerActorRef = pair._1
        val player = pair._2

        playerActorRef ! Play(player, sender)
      }
      originalSender ! Initialized(playerActorRefs)

    case PlayerReply(player, _) =>
      if (isLastPlayer(numberOfPlayers)) {
        val gameResultAggregatorActorRef = context.actorOf(GameResultAggregatorActor.props(new OnAirResultAggregator(new OnAirResultCalculator())))
        gameResultAggregatorActorRef ! AggregateResults(playersWithRandomNumbers)
      } else context.become(withState(numberOfPlayers - 1, player :: playersWithRandomNumbers))

    case AggregatorReply(results) =>
      originalSender ! Aggregated(results)
      log.info(results.mkString(start = "\n", sep = "\n", end = ""))

  }

  private final def isLastPlayer(numberOfPlayers: Int): Boolean = numberOfPlayers == 0
}


object GameActor {
  final case class InitializePlayers(numberOfPlayers: Int)
  final case class Initialized(playerActorRefs: IndexedSeq[ActorRef])
  final case class Aggregated(result: Seq[AggregatedResult])
}
