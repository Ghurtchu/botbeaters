package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.Player

trait MultiRandomNumbersGenerator {
  def generate(players: List[Player]): List[Player]
}
