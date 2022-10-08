package com.onairentertainment.delivery.akka.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.core.model.Player
import com.onairentertainment.core.service.implementation.BoundedRandomNumberGenerator
import com.onairentertainment.delivery.akka.actors.game.PlayerActor
import com.onairentertainment.delivery.akka.actors.game.PlayerActor.{Play, PlayerReply}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._

class PlayerActorTestSpec extends TestKit(ActorSystem("PlayerActorTestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  val sys: ActorSystem = ActorSystem("PlayerActorTestSystem")

  override protected def afterAll(): Unit = sys.terminate()

  "A PlayerActor" should {

    "accept Play message and return PlayerReply to the sender" in {
      val playerActor = sys.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 10_000)))
      val player = Player()
      playerActor ! Play(player)
      expectMsgType[PlayerReply]
    }

    "accept Play message and return PlayerReply with the Player's updated random number" in {
      val playerActor = sys.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 10_000)))
      val player = Player()
      playerActor ! Play(player)
      val reply = expectMsgType[PlayerReply]
      assert(reply.player.randomNumber.isDefined)
    }

    "not handle any other messages than Play" in {
      val playerActor = sys.actorOf(PlayerActor.props(new BoundedRandomNumberGenerator(from = 0, to = 10_000)))
      playerActor ! 5
      playerActor ! true
      playerActor ! IndexedSeq()
      playerActor ! "Akka rocks!"
      expectNoMessage(3.seconds)
    }

  }



}
