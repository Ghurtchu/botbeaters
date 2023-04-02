package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.Player

trait MultiRNGService {
  def gen(players: List[Player]): List[Player]
}
