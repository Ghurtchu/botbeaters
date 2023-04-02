package com.onairentertainment.delivery.console

import com.onairentertainment.core.domain.{AggregatedResult, Player}
import com.onairentertainment.core.service.implementation.{MultiRNG, BoundedRNG, OnAirAggregator, OnAirCalculator}
import com.onairentertainment.core.service.protocol.GameAggregator

object ConsoleApp extends scala.App {

  sealed trait GameState

  object GameState {
    case object On extends GameState
    case object Off extends GameState
  }

  println("Welcome to the game!")

  during(GameState.On) {
    val bot = Player(id = "bot")
    val players = bot :: List.fill(5)(Player())
    val rng = new MultiRNG(new BoundedRNG(from = 0, to = 10_000))
    val playersUpdated = rng.gen(players)
    val aggreg = new OnAirAggregator(new OnAirCalculator())
    val res = aggreg.aggr(playersUpdated)

    println(res.mkString("\n"))
    println(s"<${"~" * 50}>")
    Thread sleep 3000 // play every 3 secs
  }

  def during(gameState: => GameState)(game: => Unit): Unit =
    while (gameState == GameState.On)
      game

}
