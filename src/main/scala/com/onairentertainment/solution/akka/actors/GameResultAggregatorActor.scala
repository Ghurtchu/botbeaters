package com.onairentertainment.solution.akka.actors

import akka.actor._
import com.onairentertainment.core.model.{AggregatedResult, Player}
import com.onairentertainment.core.service.protocol.GameResultAggregator
import com.onairentertainment.solution.akka.actors.GameResultAggregatorActor.{AggregateResults, AggregatorReply}

import scala.collection.mutable.ArrayBuffer

class GameResultAggregatorActor(gameResultAggregator: GameResultAggregator) extends Actor with ActorLogging {
  override def receive: Receive = {
    case AggregateResults(players) =>
      log.info("Aggregating results for players")
      val gameResult = gameResultAggregator.aggregate(players.toSeq)
      sender ! AggregatorReply(gameResult)
  }
}

object GameResultAggregatorActor {

  final case class AggregateResults(players: ArrayBuffer[Player])
  final case class AggregatorReply(results: Seq[AggregatedResult])

  def props(gameResultAggregator: GameResultAggregator): Props =
    Props(new GameResultAggregatorActor(gameResultAggregator))

}
