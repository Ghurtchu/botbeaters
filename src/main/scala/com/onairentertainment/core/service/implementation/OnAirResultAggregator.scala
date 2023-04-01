package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.domain.{AggregatedResult, IntermediateResult, Player, RandomNumber}
import com.onairentertainment.core.service.protocol.{GameResultAggregator, GameResultCalculator}


final class OnAirResultAggregator(private val gameResultCalculator: GameResultCalculator) extends GameResultAggregator {

  override def aggregate(players: List[Player]): List[AggregatedResult] = {
    val sortedAndFilteredPlayers = (for {
      intermediateResults <- Option(players.map(toIntermediateResult))
      bot                 <- intermediateResults.find(_.player == "bot")
      botBeaterPlayers    =  bot :: intermediateResults.filter(_.result > bot.result) // we could have used less stricter equality like ">=" here, depends on the requirements.
      sortedPlayers       =  botBeaterPlayers
          .sortWith(_.result > _.result)
          .zipWithIndex
          .map(toAggregatedResult)
    } yield sortedPlayers).fold(List.empty[AggregatedResult])(identity)

    sortedAndFilteredPlayers
  }

  private def toIntermediateResult(player: Player): IntermediateResult = {
    val name = player.id
    val number = player.randomNumber.fold(0)(_.value)
    val result = gameResultCalculator.calculate(player).value

    IntermediateResult(name, number, result)
  }

  private def toAggregatedResult: ((IntermediateResult, Int)) => AggregatedResult = {
    case (intermediateResult, position) =>
      AggregatedResult(position + 1, intermediateResult)
  }

}
