package com.onairentertainment.solution.console

import com.onairentertainment.core.model.{AggregatedResult, Player}
import com.onairentertainment.core.service.implementation.{BasicMultiRandomNumbersGenerator, BoundedRandomNumberGenerator, OnAirResultAggregator, OnAirResultCalculator}
import com.onairentertainment.core.service.protocol.GameResultAggregator

object ScalaConsoleApp extends scala.App {

  sealed trait GameState

  object GameState {
    case object On extends GameState
    case object Off extends GameState
  }

  println("Welcome to the game!")

  setGameState(GameState.On) {
    val initialPlayers: Seq[Player] = for (_ <- 0 to 10) yield Player()
    val multiRandomNumbersGenerator = new BasicMultiRandomNumbersGenerator(new BoundedRandomNumberGenerator(from = 0, to = 10_000))
    val playersWithNumbers: Seq[Player] = multiRandomNumbersGenerator.generate(initialPlayers)
    val gameAggregator: GameResultAggregator = new OnAirResultAggregator(new OnAirResultCalculator())
    val gameResult: Seq[AggregatedResult] = gameAggregator.aggregate(playersWithNumbers)

    println(gameResult.mkString("\n"))
    println(s"<${"~" * 50}>")
    Thread sleep 3000 // play every 3 secs
  }

  def setGameState(gameState: => GameState)(game: => Unit): Unit = while (gameState == GameState.On) game

}
