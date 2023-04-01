package com.onairentertainment.core

object domain {

  final case class GameResult(value: Int) extends AnyVal

  final case class AggregatedResult(position: Int, player: String, number: Int, result: Int)

  object AggregatedResult {
    def apply(position: Int, intermediateResult: IntermediateResult): AggregatedResult =
      AggregatedResult(position, intermediateResult.player, intermediateResult.number, intermediateResult.result)
  }

  final case class IntermediateResult(player: String, number: Int, result: Int)

  final case class Player(id: String, randomNumber: Option[RandomNumber])

  object Player {

    def apply(): Player =
      new Player(s"player_${java.util.UUID.randomUUID().toString}", None)

    def apply(id: String): Player = new Player(id, None)
  }

  final case class RandomNumber(value: Int) extends AnyVal

  final case class PlayPayload(messageType: String, players: Int)

}
