package com.onairentertainment.delivery.akka.actors.game

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.implementation.BoundedRandomNumberGenerator
import com.onairentertainment.delivery.akka.actors.game.RandomNumberGeneratorActor.{GenerateRandomNumber, PlayerUpdated}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.mutable
import scala.concurrent.duration.DurationInt

class RandomNumberGeneratorActorSpec extends TestKit(ActorSystem("PlayerActorTestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  val sys: ActorSystem = ActorSystem("PlayerActorTestSystem")

  "A RandomNumberGeneratorActor" should {
    "receive GenerateRandomNumber(player) and respond with UpdatePlayer(player) with random number in it" in {
      val actor = sys.actorOf(RandomNumberGeneratorActor.props(new BoundedRandomNumberGenerator(from = 0, to = 999_999)))
      val player = Player(id = "bot")
      actor ! GenerateRandomNumber(player)
      val expected = expectMsgType[PlayerUpdated]
      assert(expected.player.randomNumber.isDefined)
    }
    "not handle any other messages than GenerateRandomNumber(player)" in {
      val actor = sys.actorOf(RandomNumberGeneratorActor.props(new BoundedRandomNumberGenerator(from = 0, to = 999_999)))
      actor ! 123
      actor ! "true"
      actor ! false
      actor ! mutable.LinkedHashMap
      expectNoMessage(3.seconds)
    }
  }
}
