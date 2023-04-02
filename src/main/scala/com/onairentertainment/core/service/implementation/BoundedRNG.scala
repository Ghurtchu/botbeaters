package com.onairentertainment.core.service.implementation

import BoundedRNG.{InvalidBoundException, underlyingImpl}
import com.onairentertainment.core.domain.RandomNumber
import com.onairentertainment.core.service.protocol.RNG

import scala.util.Random

final class BoundedRNG(from: Int, to: Int) extends RNG {

  override def gen: RandomNumber = {
    if (from >= to) throw new InvalidBoundException("Invalid bounds. `to` must be greater than `from`")

    RandomNumber(underlyingImpl.between(from, to))
  }
}

object BoundedRNG {
  protected final val underlyingImpl = new Random
  class InvalidBoundException(override val getMessage: String) extends RuntimeException
}
