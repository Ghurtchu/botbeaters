package com.onairentertainment

import com.onairentertainment.delivery.akka.{AkkaConsoleApp, AkkaHttpApp}
import com.onairentertainment.delivery.console.ConsoleApp

object GameBootstrap {

  sealed trait Solution

  object Solution {

    case object AkkaWebSockets extends Solution
    case object AkkaConsole    extends Solution
    case object Console        extends Solution

    def apply(solution: String): Solution = {
      if (solution == "akka-websockets") AkkaWebSockets
      else if (solution == "akka-console") AkkaConsole
      else Console
    }
  }

  def main(args: Array[String]): Unit =
    args.headOption
      .fold[Solution](Solution.Console)(Solution.apply) match {
      case Solution.AkkaWebSockets => AkkaHttpApp.main(args)
      case Solution.AkkaConsole    => AkkaConsoleApp.main(args)
      case Solution.Console   => ConsoleApp.main(args)
    }

}
