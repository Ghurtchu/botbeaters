package com.onairentertainment.delivery.akka

import akka.actor.{ActorSystem, Props}
import com.onairentertainment.delivery.akka.actors.GameActor
import com.onairentertainment.delivery.akka.actors.GameActor.InitializePlayers

object AkkaConsoleApp extends scala.App {

  val system = ActorSystem("AkkaGameSystem")

  while (true) {
    val gameActor = system.actorOf(Props[GameActor])
    println("Welcome to the game!")
    gameActor ! InitializePlayers(5)
    Thread sleep 3000 // play every three seconds
    println(s"<${"~" * 50}>")
  }

}
