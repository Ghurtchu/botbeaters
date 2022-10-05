package com.onairentertainment.core.model

import scala.util.Random

final case class Player(id: String, randomNumber: Option[RandomNumber])

object Player {

  private final lazy val RANDOM = new Random()

  def apply(): Player =
    new Player(s"player_${RANDOM.between(0, Int.MaxValue)}", None)
}
