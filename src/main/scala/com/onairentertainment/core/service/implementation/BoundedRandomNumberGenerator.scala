package com.onairentertainment.core.service.implementation

import BoundedRandomNumberGenerator.{InvalidBoundException, underlyingImpl}
import com.onairentertainment.core.model.RandomNumber
import com.onairentertainment.core.service.protocol.RandomNumberGenerator

import scala.util.Random

final class BoundedRandomNumberGenerator(from: Int, to: Int) extends RandomNumberGenerator {

  override def generate: RandomNumber = {
    if (from >= to) throw new InvalidBoundException("Invalid bounds. `to` must be greater than `from`")
    RandomNumber(underlyingImpl.between(from, to))
  }
}

object BoundedRandomNumberGenerator {
  protected final val underlyingImpl: Random = new Random()
  class InvalidBoundException(override val getMessage: String) extends RuntimeException
}
