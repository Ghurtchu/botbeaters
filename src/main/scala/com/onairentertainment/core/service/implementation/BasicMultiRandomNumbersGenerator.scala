package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.model.Player
import com.onairentertainment.core.service.protocol.{MultiRandomNumbersGenerator, RandomNumberGenerator}

final class BasicMultiRandomNumbersGenerator(private val randomNumberGenerator: RandomNumberGenerator) extends MultiRandomNumbersGenerator {

  override def generate(players: Seq[Player]): Seq[Player] =
    players.map(_.copy(randomNumber = Some(randomNumberGenerator.generate)))
}
