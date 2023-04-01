package com.onairentertainment.delivery.akka

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.onairentertainment.delivery.akka.actors.game.GameActor.{GameResult, InitializeGame}
import com.onairentertainment.delivery.akka.actors.game.GameActor

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object AkkaConsoleApp extends scala.App {

  val system = ActorSystem("AkkaGameSystem")

  implicit val timeout: Timeout = Timeout(2.seconds)
  import system.dispatcher

  while (true) {
    val gameActor = system.actorOf(Props[GameActor])
    println("-" * 44 + "GAME STARTED" + "-" * 44)
    (gameActor ? InitializeGame(5)).mapTo[GameResult].onComplete {
      case Success(value) =>
        println("-" * 46 + "RESULTS" + "-" * 46)
        println(value.results.mkString(sep = "\n"))
        println("-" * 99)
      case Failure(exception) => println(s"Failed due to $exception")
    }
    Thread sleep 3000 // play every three seconds
  }

}
