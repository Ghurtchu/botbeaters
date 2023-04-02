package com.onairentertainment.core.service.implementation

import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.protocol.{MultiRNGService, RNGService}

final class SimpleRNG(gen: RNGService)
  extends MultiRNGService {

  override def gen(players: List[Player]): List[Player] =
    players.map(_.copy(randomNumber = Some(gen.gen)))
}
