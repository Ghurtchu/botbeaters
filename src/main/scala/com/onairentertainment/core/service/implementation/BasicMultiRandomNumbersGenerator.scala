package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.protocol.{MultiRandomNumbersGenerator, RandomNumberGenerator}

final class BasicMultiRandomNumbersGenerator(gen: RandomNumberGenerator) extends MultiRandomNumbersGenerator {

  override def generate(players: List[Player]): List[Player] =
    players.map(_.copy(randomNumber = Some(gen.generate)))
}
