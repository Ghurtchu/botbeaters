package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.model.{AggregatedResult, Player, RandomNumber}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class OnAirResultAggregatorTest extends AnyWordSpecLike with Matchers { {

  "An OnAirResultAggregator" should {
    "aggregate results based on players' score and return them sorted in ascending order by the accumulated score" in {
      val service = new OnAirResultAggregator(new OnAirResultCalculator())
      val botPlayer = new Player(id = "bot", randomNumber = Some(RandomNumber(99999)))
      val players = botPlayer :: (for (_ <- 1 to 10) yield Player()).map(player => player.copy(randomNumber = Some(new BoundedRandomNumberGenerator(from = 10_000, to = 100_000).generate))).toList
      val sorted = service.aggregate(players)
      assertResult(sorted.sortWith(_.result > _.result))(sorted)
    }
    "should only return the bot result because it's the highest" in {
      val service = new OnAirResultAggregator(new OnAirResultCalculator())
      val botPlayer = new Player(id = "bot", randomNumber = Some(RandomNumber(99999)))
      val players = botPlayer :: (for (_ <- 1 to 10) yield Player()).map(player => player.copy(randomNumber = Some(new BoundedRandomNumberGenerator(from = 0, to = 10).generate))).toList
      val sorted = service.aggregate(players)
      val expected = AggregatedResult(1, "bot", 99999, 10_000)
      assertResult(expected)(sorted.head)
    }
    "should return the list in the following order: player2 -> player3 -> bot, excluding player 1" in {
      val service = new OnAirResultAggregator(new OnAirResultCalculator())
      val players = Player("player1", Some(RandomNumber(5))) :: Player("player2", Some(RandomNumber(11112))) :: Player("player3", Some(RandomNumber(11111))) :: Player("bot", Some(RandomNumber(11123))) :: Nil
      val sorted = service.aggregate(players)
      val sortedNames = sorted.map(_.player)
      val expected = "player3" :: "player2" :: "bot" :: Nil
      assertResult(expected)(sortedNames)
    }
  }
}

}
