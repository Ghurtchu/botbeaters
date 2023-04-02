package com.onairentertainment.delivery.akka.actors.game

import akka.actor._
import com.onairentertainment.core.domain.{AggregatedResult, Player}
import com.onairentertainment.core.service.protocol.GameAggregator
import com.onairentertainment.delivery.akka.actors.game.GameAggregatorActor.{AggregateResults, GameAggregatorReply}

final class GameAggregatorActor(aggr: GameAggregator) extends Actor
  with ActorLogging {

  override def receive: Receive = {

    case AggregateResults(players) => {
      log.info(s"Aggregating results for all")
      val gameResult = aggr.aggr(players)

      sender ! GameAggregatorReply(gameResult)
    }
  }

}

object GameAggregatorActor {

  final case class AggregateResults(players: List[Player])
  final case class GameAggregatorReply(results: List[AggregatedResult])

  def props(aggr: GameAggregator): Props =
    Props(new GameAggregatorActor(aggr))

}
