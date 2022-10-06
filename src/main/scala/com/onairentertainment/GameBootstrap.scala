package com.onairentertainment

import com.onairentertainment.delivery.akka.AkkaConsoleApp
import com.onairentertainment.delivery.console.ScalaConsoleApp

import scala.util.Try

object GameBootstrap {

  sealed trait Solution

  object Solution {

    case object AkkaWebSockets extends Solution
    case object AkkaConsole    extends Solution
    case object ScalaConsole   extends Solution

    def apply(solution: String): Solution = {
      if (solution == "akka-http") AkkaWebSockets
      else if (solution == "akka-console") AkkaConsole
      else ScalaConsole
    }
  }

  def main(args: Array[String]): Unit = {
    for (arg <- Try(args(0))) {
      Solution(arg) match { // run ScalaConsoleApp as a default choice
        case Solution.AkkaConsole    => AkkaConsoleApp.main(args)
        case Solution.ScalaConsole   => ScalaConsoleApp.main(args)
        case Solution.AkkaWebSockets => // not yet implemented
      }
    }
  }

}
