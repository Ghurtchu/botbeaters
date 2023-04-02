package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.domain.{AggregatedResult, IntermediateResult, Player}
import com.onairentertainment.core.service.protocol.{GameAggregator, GameResultCalculator}


final class OnAirAggregator(private val calc: GameResultCalculator) extends GameAggregator {

  override def aggr(players: List[Player]): List[AggregatedResult] = {
    val aggregated = (for {
      intermediate <- Option(players.map(toIntermediate))
      bot <- intermediate.find(_.player == "bot")
      // we could have used less stricter equality like ">=" here, depends on the requirements.
      botBeaters = bot :: intermediate.filter(_.result > bot.result)
      sorted =  botBeaters
        .sortWith(_.result > _.result)
        .zipWithIndex
        .map(toAggregatedResult)
    } yield sorted).getOrElse(Nil)

    aggregated
  }

  private def toIntermediate(player: Player): IntermediateResult = {
    val name = player.id
    val number = player.randomNumber.fold(0)(_.value)
    val result = calc.calc(player).value

    IntermediateResult(name, number, result)
  }

  private def toAggregatedResult: ((IntermediateResult, Int)) => AggregatedResult = {
    case (intermediateResult, position) =>
      AggregatedResult(position + 1, intermediateResult)
  }

}
