package com.onairentertainment.core.service.implementation

import OnAirCalculator.{EMPTY_STRING, NUM_RANGE}
import com.onairentertainment.core.domain.{GameResult, Player}
import com.onairentertainment.core.service.protocol.GameResultCalculator

final class OnAirCalculator extends GameResultCalculator {

  override def calc(player: Player): GameResult = {
    val randomNumberAsString = player.randomNumber.fold("0")(_.value.toString)
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
      .map { case (_, score) =>  applyFormula(score) }
      .sum
  }

  private def applyFormula(score: Int): Int = Math.pow(10, score - 1).toInt

}

object OnAirCalculator {
  private final lazy val EMPTY_STRING: String   = ""
  private final lazy val NUM_RANGE: Seq[String] = (0 to 9).map(_.toString)
}
