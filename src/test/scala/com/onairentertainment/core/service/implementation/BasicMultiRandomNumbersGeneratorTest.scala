package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.model.Player
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BasicMultiRandomNumbersGeneratorTest extends AnyWordSpecLike with Matchers {

  "A BasicMultiRandomNumbersGenerator" should {

    "generate random numbers for all players which are passed in" in {
      val service = new BasicMultiRandomNumbersGenerator(new BoundedRandomNumberGenerator(from = 10, to = 100))
      val players = for (_ <- 1 to 100) yield Player()
      val updatedPlayers = service.generate(players)
      assert(updatedPlayers.forall(_.randomNumber.isDefined))
    }

    "generate correct random numbers for all players which are passed in" in {
      val service = new BasicMultiRandomNumbersGenerator(new BoundedRandomNumberGenerator(from = 10, to = 100))
      val players = for (_ <- 1 to 100) yield Player()
      val updatedPlayers = service.generate(players)
      assert(updatedPlayers.forall(p => p.randomNumber.isDefined && p.randomNumber.get.value >= 10 && p.randomNumber.get.value < 100))
    }

  }

}
