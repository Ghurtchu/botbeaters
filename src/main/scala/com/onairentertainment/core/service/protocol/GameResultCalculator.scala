package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.{GameResult, Player}

trait GameResultCalculator {
  def calc(player: Player): GameResult
}
