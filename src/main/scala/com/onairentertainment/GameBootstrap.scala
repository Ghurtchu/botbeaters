package com.onairentertainment

import com.onairentertainment.solution.akka.AkkaConsoleApp
import com.onairentertainment.solution.console.ScalaConsoleApp

import scala.util.Try

object GameBootstrap {

  sealed trait Solution

  object Solution {

    case object AkkaWebSockets extends Solution
    case object AkkaConsole    extends Solution
    case object ScalaConsole   extends Solution

    def apply(solution: String): Option[Solution] = {
      if (solution == "akka-http") Some(AkkaWebSockets)
      else if (solution == "akka-console") Some(AkkaConsole)
      else Some(ScalaConsole)
    }
  }

  def main(args: Array[String]): Unit = {
    for (arg <- Try(args(0))) {
      Solution(arg).fold(ScalaConsoleApp.main(args)) { // run ScalaConsoleApp as a default choice
        case Solution.AkkaConsole    => AkkaConsoleApp.main(args)
        case Solution.ScalaConsole   => ScalaConsoleApp.main(args)
        case Solution.AkkaWebSockets => // not yet implemented
      }
    }
  }

}
