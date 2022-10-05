package com.onairentertainment.core.service.implementation

import BoundedRandomNumberGenerator.underlyingImpl
import com.onairentertainment.core.model.RandomNumber
import com.onairentertainment.core.service.protocol.RandomNumberGenerator

import scala.util.Random

final class BoundedRandomNumberGenerator(from: Int, to: Int) extends RandomNumberGenerator {

  override def generate: RandomNumber =
    RandomNumber(underlyingImpl.between(from, to))
}

object BoundedRandomNumberGenerator {
  protected final val underlyingImpl: Random = new Random()
}
