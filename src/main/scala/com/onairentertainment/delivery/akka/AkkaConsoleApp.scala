package com.onairentertainment.delivery.akka

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.onairentertainment.delivery.akka.actors.GameActor
import com.onairentertainment.delivery.akka.actors.GameActor.InitializePlayers

import scala.concurrent.duration.DurationInt

object AkkaConsoleApp extends scala.App {

  val system = ActorSystem("AkkaGameSystem")

  implicit val timeout = Timeout(5.seconds)

  while (true) {
    val gameActor = system.actorOf(Props[GameActor])
    println("Welcome to the game!")
    gameActor ! InitializePlayers(5)
    Thread sleep 3000 // play every three seconds
    println(s"<${"~" * 50}>")
  }

}
