package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.domain.{AggregatedResult, Player, RandomNumber}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class OnAirAggregatorTest extends AnyWordSpecLike with Matchers { {

  "An OnAirResultAggregator" should {
    "aggregate results based on players' score and return them sorted in ascending order by the accumulated score" in {
      val service = new OnAirAggregator(new OnAirCalculator())
      val botPlayer = new Player(id = "bot", randomNumber = Some(RandomNumber(99999)))
      val players = botPlayer :: (for (_ <- 1 to 10) yield Player()).map(player => player.copy(randomNumber = Some(new BoundedRNG(from = 10_000, to = 100_000).gen))).toList
      val sorted = service.aggr(players)

      assertResult(sorted.sortWith(_.result > _.result))(sorted)
    }
    "should only return the bot result because it's the highest" in {
      val service = new OnAirAggregator(new OnAirCalculator())
      val botPlayer = new Player(id = "bot", randomNumber = Some(RandomNumber(99999)))
      val players = botPlayer :: (for (_ <- 1 to 10) yield Player()).map(player => player.copy(randomNumber = Some(new BoundedRNG(from = 0, to = 10).gen))).toList
      val sorted = service.aggr(players)
      val expected = AggregatedResult(1, "bot", 99999, 10_000)

      assertResult(expected)(sorted.head)
    }
    "should return the list in the following order: player2 -> player3 -> bot, excluding player 1" in {
      val service = new OnAirAggregator(new OnAirCalculator())
      val players = Player("player1", Some(RandomNumber(5))) :: Player("player2", Some(RandomNumber(11112))) :: Player("player3", Some(RandomNumber(11111))) :: Player("bot", Some(RandomNumber(11123))) :: Nil
      val sorted = service.aggr(players)
      val sortedNames = sorted.map(_.player)
      val expected = "player3" :: "player2" :: "bot" :: Nil

      assertResult(expected)(sortedNames)
    }
  }
}

}
