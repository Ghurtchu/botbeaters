package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.protocol.{MultiRNGService, RNG}

final class MultiRNG(gen: RNG) extends MultiRNGService {

  override def gen(players: List[Player]): List[Player] =
    players.map(_.copy(randomNumber = Some(gen.gen)))
}
