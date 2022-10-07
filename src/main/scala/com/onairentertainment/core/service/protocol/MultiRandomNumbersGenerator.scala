package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.model.Player

trait MultiRandomNumbersGenerator {
  def generate(players: List[Player]): List[Player]
}
