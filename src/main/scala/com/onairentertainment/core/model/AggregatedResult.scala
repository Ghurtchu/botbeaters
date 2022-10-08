package com.onairentertainment.core.model

final case class AggregatedResult(position: Int, player: String, number: Int, result: Int)

object AggregatedResult {
  def apply(position: Int, intermediateResult: IntermediateResult): AggregatedResult =
    AggregatedResult(position, intermediateResult.player, intermediateResult.number, intermediateResult.result)
}
