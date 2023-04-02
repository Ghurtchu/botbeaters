package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.onairentertainment.core.domain.{AggregatedResult, Player}
import com.onairentertainment.core.service.implementation.{
  BoundedRNG,
  OnAirAggregator,
  OnAirCalculator
}
import com.onairentertainment.delivery.akka.actors.game.GameActor._
import com.onairentertainment.delivery.akka.actors.game.GameAggregatorActor._
import com.onairentertainment.delivery.akka.actors.game.PlayerActor.{Play, PlayerReply}

final class GameActor extends Actor with ActorLogging {

  override def receive: Receive = withState(0, Nil, None)

  private def withState(playerCount: Int, players: List[Player], originalSender: Option[ActorRef]): Receive = {

    case InitializeGame(nOfPlayers) => {
      log.info(s"Initializing the game for $nOfPlayers players")
      val bot = spawnBot
      val botActor = spawnBotActor(bot.id)
      val players = spawnPlayers(nOfPlayers)
      val playerActors = botActor :: spawnPlayerActors(players)
      context.become {
        withState(
          playerCount = nOfPlayers + 1,
          players = players,
          originalSender = Some(sender())
        )
      }
      val actorPlayerPairs = playerActors.zip(bot :: players)

      actorPlayerPairs.foreach {
        case (playerActor, player) =>
          playerActor ! Play(player)
      }
    }

    case PlayerReply(player) => {
      if (isLastPlayer(playerCount)) {
        val calculator = new OnAirCalculator
        val aggregator = new OnAirAggregator(calculator)
        val gameAggregatorActor = context.actorOf(GameAggregatorActor.props(aggregator))

        gameAggregatorActor ! AggregateResults(player :: players)
      } else {
        context.become {
          withState(
            playerCount = playerCount - 1,
            players = player :: players,
            originalSender = originalSender
          )
        }
      }
    }

    case GameAggregatorReply(results) =>
      originalSender.foreach(_ ! GameResult(results))
  }

  private def isLastPlayer(numberOfPlayers: Int): Boolean =
    numberOfPlayers == 1

  private def spawnBot: Player =
    Player(id = "bot")

  private def spawnBotActor(actorName: String): ActorRef =
    context.actorOf(PlayerActor.props(rngService), actorName)

  private def spawnPlayers(amount: Int): List[Player] =
    List.fill(amount)(Player())

  private def spawnPlayerActors(players: List[Player]): List[ActorRef] =
    players.map { player =>
      context.actorOf(PlayerActor.props(rngService), s"player${player.id}")
    }

  private def rngService =
    new BoundedRNG(from = 0, to = 999999)

}

object GameActor {
  final case class InitializeGame(numberOfPlayers: Int)
  final case class GameResult(results: List[AggregatedResult])
}
