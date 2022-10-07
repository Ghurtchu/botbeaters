package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.model.{AggregatedResult, Player}

trait GameResultAggregator {
  def aggregate(players: List[Player]): List[AggregatedResult]
}
