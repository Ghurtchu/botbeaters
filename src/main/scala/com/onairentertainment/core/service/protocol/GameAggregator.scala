package com.onairentertainment.core.service.protocol

import com.onairentertainment.core.domain.{AggregatedResult, Player}

trait GameAggregator {
  def aggr(players: List[Player]): List[AggregatedResult]
}
