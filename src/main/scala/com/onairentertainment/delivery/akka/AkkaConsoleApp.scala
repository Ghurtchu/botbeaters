package com.onairentertainment.delivery.akka

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.onairentertainment.delivery.akka.actors.GameActor
import com.onairentertainment.delivery.akka.actors.GameActor.{Aggregated, InitializePlayers}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object AkkaConsoleApp extends scala.App {

  val system = ActorSystem("AkkaGameSystem")

  implicit val timeout = Timeout(2.seconds)
  import system.dispatcher

  while (true) {
    val gameActor = system.actorOf(Props[GameActor])
    println("Welcome to the game!")
    (gameActor ? InitializePlayers(5)).mapTo[Aggregated].onComplete {
      case Success(value) => println(value.results.mkString(start = "\n", sep = "\n", end = ""))
      case Failure(exception) => println(s"Failed due to $exception")
    }
    Thread sleep 3000 // play every three seconds
    println(s"<${"~" * 50}>")
  }

}
