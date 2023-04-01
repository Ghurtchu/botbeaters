package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.onairentertainment.core.domain.{AggregatedResult, Player}
import com.onairentertainment.core.service.implementation.{BoundedRandomNumberGenerator, OnAirResultAggregator, OnAirResultCalculator}
import com.onairentertainment.delivery.akka.actors.game.GameActor._
import com.onairentertainment.delivery.akka.actors.game.GameResultAggregatorActor._
import com.onairentertainment.delivery.akka.actors.game.PlayerActor.{Play, PlayerReply}

final class GameActor extends Actor with ActorLogging {

  override def receive: Receive = withState(0, Nil, None)

  private def withState(numberOfPlayers: Int, playersWithRandomNumbers: List[Player], originalSender: Option[ActorRef]): Receive = {

    case InitializeGame(nOfPlayers) =>
      log.info(s"Initializing the game for $nOfPlayers players")
      val botPlayer = spawnBotPlayer
      val botPlayerActorRef = spawnBotPlayerActor(botPlayer.id)
      val players = spawnPlayers(nOfPlayers)
      val playerActorRefs = botPlayerActorRef :: spawnPlayerActorRefs(players)
      context.become(withState(nOfPlayers + 1, playersWithRandomNumbers, Some(sender())))
      playerActorRefs.zip(botPlayer :: players).foreach { case (playerActorRef, player) => playerActorRef ! Play(player) }

    case PlayerReply(player) =>
      if (isLastPlayer(numberOfPlayers)) {
        val gameResultAggregatorActorRef = context.actorOf(GameResultAggregatorActor.props(new OnAirResultAggregator(new OnAirResultCalculator())))
        gameResultAggregatorActorRef ! AggregateResults(player :: playersWithRandomNumbers)
      } else context.become(withState(numberOfPlayers - 1, player :: playersWithRandomNumbers, originalSender))


    case GameAggregatorReply(results) => originalSender.foreach(_ ! GameResult(results))

  }

  private def isLastPlayer(numberOfPlayers: Int): Boolean =
    numberOfPlayers == 1

  private def spawnBotPlayer: Player =
    Player(id = "bot")

  private def spawnBotPlayerActor(actorName: String): ActorRef =
    context.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 999999)), actorName)

  private def spawnPlayers(amount: Int): List[Player] =
    (for (_ <- 1 to amount) yield Player()).toList

  private def spawnPlayerActorRefs(players: List[Player]): List[ActorRef] =
    for (player <- players) yield context.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 999999)), s"player${player.id}")

}


object GameActor {
  final case class InitializeGame(numberOfPlayers: Int)
  final case class GameResult(results: List[AggregatedResult])
}
