package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.{GameResult, Player}

trait GameResultCalculator {
  def calculate(player: Player): GameResult
}
