package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.domain.{Player, RandomNumber}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class OnAirResultCalculatorTest extends AnyWordSpecLike with Matchers {

  // Formula: sum(10 ^ (times(i) - 1)) where i is the single variable number in random number.

  "An OnAirResultCalculator" should {
    "Calculate 10_000 if the player's random number is `11111` or `22222` or `33333` ... `99999`" in {
      val service = new OnAirResultCalculator()
      val players = for (i <- 11111 to 99999 by 11111) yield new Player(id = s"$i", randomNumber = Some(RandomNumber(i)))
      for (player <- players) {
        val score = service.calculate(player)
        assertResult(10_000)(score.value)
      }
    }
    "Calculate `5` if the player's random number is `12345`" in {
      val service = new OnAirResultCalculator()
      val player = new Player(id = "1", randomNumber = Some(RandomNumber(12345)))
      val score = service.calculate(player)
      assertResult(5)(score.value)
    }
    "Calculate `12` if the player's random number has two identical and two different numbers: like `1189` or `2289` or `4489`" in {
      val service = new OnAirResultCalculator()
      val players = for (i <- 1 to 7) yield new Player(id = s"$i", randomNumber = Some(RandomNumber(s"$i${i}89".toInt)))
      val scores = players.map(service.calculate)
      assert(scores.forall(_.value == 12))
    }
  }

}
