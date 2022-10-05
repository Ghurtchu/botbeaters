package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.model.{AggregatedResult, IntermediateResult, Player}
import com.onairentertainment.core.service.protocol.{GameResultAggregator, GameResultCalculator}

final class OnAirResultAggregator(gameResultCalculator: GameResultCalculator) extends GameResultAggregator {

  override def aggregate(players: Seq[Player]): Seq[AggregatedResult] = {
    players
      .map(toIntermediateResult)
      .sortWith(_.result > _.result)
      .zipWithIndex
      .map(toAggregatedResult)
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
