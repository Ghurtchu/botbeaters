package com.onairentertainment.core.service.implementation

import OnAirResultCalculator.{EMPTY_STRING, NUM_RANGE}
import com.onairentertainment.core.model.{GameResult, Player}
import com.onairentertainment.core.service.protocol.GameResultCalculator

class OnAirResultCalculator extends GameResultCalculator {

  override def calculate(player: Player): GameResult = {
    val randomNumberAsString = player.randomNumber.get.value.toString
    val playerScore = calculatePlayerScore(randomNumberAsString)

    GameResult(playerScore)
  }

  private def calculatePlayerScore(randomNumberAsString: String): Int = {
    (for {
      number <- NUM_RANGE
      count  = randomNumberAsString.count(_.toString == number)
    } yield number.toInt -> count)
      .toMap
      .filter(_._2 != 0)
      .map(applyFormula)
      .sum
  }

  private def applyFormula(pair: (Int, Int)): Int = Math.pow(10, pair._2 - 1).toInt

}

object OnAirResultCalculator {
  private final lazy val EMPTY_STRING: String   = ""
  private final lazy val NUM_RANGE: Seq[String] = (0 to 9).map(_.toString)
}
