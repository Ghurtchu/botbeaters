package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.model.{Player, RandomNumber}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class OnAirResultCalculatorTest extends AnyWordSpecLike with Matchers {

  // Formula: sum(10 ^ (times(i) - 1)) where i is the single variable number in random number.

  "An OnAirResultCalculator" should {

    // in case of all `1`-s the formula will do the following:
    // 10 ^ (5 - 1) = 10 ^ 4 = 10_000
    "Calculate 10_000 if the player's random number is `11111` or `22222` or `33333` ... `99999`" in {
      val service = new OnAirResultCalculator()
      val players = for (i <- 11111 to 99999 by 11111) yield new Player(id = s"$i", randomNumber = Some(RandomNumber(i)))
      for (player <- players) {
        val score = service.calculate(player)
        assertResult(10_000)(score.value)
      }
    }

    // in case of `12345` the formula will do the following:
    // 10 ^ (1 - 1) + 10 ^ (1 - 1) + ... + 10 ^ (1 - 1) = 1 + 1 + ... + 1 = 5
    "Calculate `5` if the player's random number is `12345`" in {
      val service = new OnAirResultCalculator()
      val player = new Player(id = "1", randomNumber = Some(RandomNumber(12345)))
      val score = service.calculate(player)
      assertResult(5)(score.value)
    }

  }

}
