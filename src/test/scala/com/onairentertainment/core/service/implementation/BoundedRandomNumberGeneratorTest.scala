package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.service.implementation.BoundedRandomNumberGenerator.InvalidBoundException
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class BoundedRandomNumberGeneratorTest extends AnyWordSpecLike with Matchers {

  "A BoundedRandomNumberGenerator" should {

    "generate either 0 or 1 if the bounds are equal to [0, 1)" in {
      val service = new BoundedRandomNumberGenerator(from = 0, to = 1)
      val randomNumbers = for (_ <- 1 to 100) yield service.generate
      randomNumbers.forall(n => n.value == 0 || n.value == 1)
    }

    "throw InvalidBoundException if the bounds are equal to [1, 1)" in {
      for (i <- 1 to 100) {
        val service = new BoundedRandomNumberGenerator(from = i, to = i)
        assertThrows[InvalidBoundException](service.generate)
      }
    }

    "generate a number between 5 and 14 (inclusive) if the bounds are equal to [5, 15)" in {
      val service = new BoundedRandomNumberGenerator(from = 5, to = 15)
      val randomNumbers = for (_ <- 1 to 100) yield service.generate
      randomNumbers.forall(n => n.value >= 5 && n.value < 15) // from is `inclusive`, to is `exclusive`
    }

  }

}
