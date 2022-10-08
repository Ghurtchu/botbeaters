package com.onairentertainment.delivery.akka.actors

import akka.actor._
import com.onairentertainment.core.model.{AggregatedResult, Player}
import com.onairentertainment.core.service.protocol.GameResultAggregator
import com.onairentertainment.delivery.akka.actors.GameResultAggregatorActor.{AggregateResults, AggregatorReply}

class GameResultAggregatorActor(gameResultAggregator: GameResultAggregator) extends Actor with ActorLogging {
  override def receive: Receive = {
    case AggregateResults(players) =>
      log.info(s"Aggregating results for all")
      val gameResult = gameResultAggregator.aggregate(players)
      sender ! AggregatorReply(gameResult)
  }
}

object GameResultAggregatorActor {

  final case class AggregateResults(players: List[Player])
  final case class AggregatorReply(results: List[AggregatedResult])

  def props(gameResultAggregator: GameResultAggregator): Props =
    Props(new GameResultAggregatorActor(gameResultAggregator))

}
