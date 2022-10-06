package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.model.Player
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class OnAirResultAggregatorTest extends AnyWordSpecLike with Matchers { {

  "An OnAirResultAggregator" should {
    "aggregate results based on players' score and return them sorted in ascending order by the accumulated score" in {
      val service = new OnAirResultAggregator(new OnAirResultCalculator())
      val players = (for (_ <- 1 to 10) yield Player()).map(player => player.copy(randomNumber = Some(new BoundedRandomNumberGenerator(from = 10_000, to = 100_000).generate)))
      val sorted = service.aggregate(players)
      assertResult(sorted.sortWith(_.result > _.result))(sorted)
    }
  }
}

}
