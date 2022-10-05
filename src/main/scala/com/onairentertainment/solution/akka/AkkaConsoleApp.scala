package com.onairentertainment.solution.akka

import akka.actor.{ActorSystem, Props}
import com.onairentertainment.solution.akka.actors.GameActor
import com.onairentertainment.solution.akka.actors.GameActor.InitializePlayers

object AkkaConsoleApp extends scala.App {

  val system = ActorSystem("AkkaGameSystem")

  while (true) {
    val gameInitializer = system.actorOf(Props[GameActor])
    println("Welcome to the game!")
    gameInitializer ! InitializePlayers(5)
    Thread sleep 3000 // play every three seconds
    println(s"<${"~" * 50}>")
  }

}
