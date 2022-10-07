package com.onairentertainment.delivery.akka.actors

import akka.actor.{Actor, ActorLogging}
import com.onairentertainment.core.model.{AggregatedResult, Player}
import com.onairentertainment.core.service.implementation.{BasicMultiRandomNumbersGenerator, BoundedRandomNumberGenerator, OnAirResultAggregator, OnAirResultCalculator}
import com.onairentertainment.delivery.akka.actors.GameActor.Aggregated

class WebsocketGameActor extends Actor with ActorLogging {

  import WebsocketGameActor._

  override def receive: Receive = {

    case PlayGame(playerCount) =>
      val initialPlayers: List[Player] = Player(id = "bot") :: (for (_ <- 1 to playerCount) yield Player()).toList
      val multiRandomNumbersGenerator = new BasicMultiRandomNumbersGenerator(new BoundedRandomNumberGenerator(from = 0, to = 10_000))
      val playersWithNumbers: List[Player] = multiRandomNumbersGenerator.generate(initialPlayers)
      val gameResult: List[AggregatedResult] = new OnAirResultAggregator(new OnAirResultCalculator()).aggregate(playersWithNumbers)

      sender ! Aggregated(gameResult)

  }
}

object WebsocketGameActor {
  final case class PlayGame(playerCount: Int)
}