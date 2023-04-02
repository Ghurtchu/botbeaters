package com.onairentertainment.delivery.console

import com.onairentertainment.core.domain.{AggregatedResult, Player}
import com.onairentertainment.core.service.implementation.{SimpleRNG, BoundedRNG, OnAirAggregator, OnAirCalculator}
import com.onairentertainment.core.service.protocol.GameAggregator

object ScalaConsoleApp extends scala.App {

  sealed trait GameState

  object GameState {
    case object On extends GameState
    case object Off extends GameState
  }

  println("Welcome to the game!")

  during(GameState.On) {
    val botPlayer = Player(id = "bot")
    val initialPlayers: List[Player] = botPlayer :: (for (_ <- 0 to 5) yield Player()).toList
    val multiRandomNumbersGenerator = new SimpleRNG(new BoundedRNG(from = 0, to = 10_000))
    val playersWithNumbers: List[Player] = multiRandomNumbersGenerator.gen(initialPlayers)
    val gameAggregator: GameAggregator = new OnAirAggregator(new OnAirCalculator())
    val gameResult: Seq[AggregatedResult] = gameAggregator.aggr(playersWithNumbers)

    println(gameResult.mkString("\n"))
    println(s"<${"~" * 50}>")
    Thread sleep 3000 // play every 3 secs
  }

  def during(gameState: => GameState)(game: => Unit): Unit = while (gameState == GameState.On) game

}
