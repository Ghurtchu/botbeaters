package com.onairentertainment.delivery.akka.actors.game

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.core.domain.Player
import com.onairentertainment.core.service.implementation.BoundedRNG
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
      val actor = sys.actorOf(PlayerActor.props(new BoundedRNG(from = 0, to = 10_000)))
      val player = Player()
      actor ! Play(player)

      expectMsgType[PlayerReply]
    }
    "accept Play message and return PlayerReply with the Player's updated random number" in {
      val actor = sys.actorOf(PlayerActor.props(new BoundedRNG(from = 0, to = 10_000)))
      val player = Player()
      actor ! Play(player)
      val reply = expectMsgType[PlayerReply]

      assert(reply.player.randomNumber.isDefined)
    }
    "not handle any other messages than Play" in {
      val actor = sys.actorOf(PlayerActor.props(new BoundedRNG(from = 0, to = 10_000)))
      actor ! 5
      actor ! true
      actor ! IndexedSeq()
      actor ! "Akka rocks!"

      expectNoMessage(3.seconds)
    }
  }
}
