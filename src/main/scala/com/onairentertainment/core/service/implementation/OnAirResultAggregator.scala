package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.model.{AggregatedResult, IntermediateResult, Player}
import com.onairentertainment.core.service.protocol.{GameResultAggregator, GameResultCalculator}


final class OnAirResultAggregator(private val gameResultCalculator: GameResultCalculator) extends GameResultAggregator {

  override def aggregate(players: List[Player]): List[AggregatedResult] = {
    val sortedAndFilteredPlayers = (for {
      intermediateResults <- Option(players.map(toIntermediateResult))
      bot                 <- intermediateResults.find(_.player == "bot")
      botResult           <- Option(bot.result)
      botBeaterPlayers    <- Option(bot +: intermediateResults.filter(_.result > botResult)) // we could have used less stricter equality like ">=" here, depends on the requirements.
      sortedPlayers       <- Option {
        botBeaterPlayers
          .sortWith(_.result > _.result)
          .zipWithIndex
          .map(toAggregatedResult)
      }
    } yield sortedPlayers).fold(List.empty[AggregatedResult])(identity)

    sortedAndFilteredPlayers
  }

  private def toIntermediateResult(player: Player): IntermediateResult = {
    val name = player.id
    val number = player.randomNumber.get.value
    val result = gameResultCalculator.calculate(player).value

    IntermediateResult(name, number, result)
  }

  private def toAggregatedResult(resultWithIndex: (IntermediateResult, Int)): AggregatedResult = {
    val position = resultWithIndex._2 + 1
    val intermediateResult = resultWithIndex._1

    AggregatedResult(position, intermediateResult)
  }

}
