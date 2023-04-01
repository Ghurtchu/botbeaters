package com.onairentertainment

import com.onairentertainment.delivery.akka.{AkkaConsoleApp, AkkaHttpApp}
import com.onairentertainment.delivery.console.ScalaConsoleApp

object GameBootstrap {

  sealed trait Solution

  object Solution {

    case object AkkaWebSockets extends Solution
    case object AkkaConsole    extends Solution
    case object ScalaConsole   extends Solution

    def apply(solution: String): Solution = {
      if (solution == "akka-websockets") AkkaWebSockets
      else if (solution == "akka-console") AkkaConsole
      else ScalaConsole
    }
  }

  def main(args: Array[String]): Unit =
    args.headOption
      .fold[Solution](Solution.ScalaConsole)(Solution.apply) match {
      case Solution.AkkaWebSockets => AkkaHttpApp.main(args)
      case Solution.AkkaConsole    => AkkaConsoleApp.main(args)
      case Solution.ScalaConsole   => ScalaConsoleApp.main(args)
    }

}
