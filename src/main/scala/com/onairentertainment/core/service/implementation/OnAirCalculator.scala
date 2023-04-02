package com.onairentertainment.core.service.implementation

import OnAirCalculator.NUM_RANGE
import com.onairentertainment.core.domain.{GameResult, Player}
import com.onairentertainment.core.service.protocol.GameResultCalculator

final class OnAirCalculator extends GameResultCalculator {

  override def calc(player: Player): GameResult = {
    val randStr = player.randomNumber
      .fold("0")(_.value.toString)
    val score = calculatePlayerScore(randStr)

    GameResult(score)
  }

  private def calculatePlayerScore(randString: String): Int = {
    val numCount = NUM_RANGE.map { num =>
      val occurrence = randString.count(_.toString == num)

      num.toInt -> occurrence
    }.toMap

    numCount
      .filter(_._2 != 0)
      .map { case (_, score) => applyFormula(score) }
      .sum
  }

  private def applyFormula(score: Int): Int =
    Math.pow(10, score - 1).toInt

}

object OnAirCalculator {
  private final lazy val NUM_RANGE: Seq[String] = (0 to 9).map(_.toString)
}
