package com.onairentertainment.delivery.akka.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.core.model.{AggregatedResult, Player, RandomNumber}
import com.onairentertainment.delivery.akka.actors.GameActor._
import com.onairentertainment.delivery.akka.actors.GameResultAggregatorActor.AggregatorReply
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class GameActorTestSpec extends TestKit(ActorSystem("GameResultAggregatorActorTestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  val sys: ActorSystem = ActorSystem("GameActorTestSystem")

  override protected def afterAll(): Unit = sys.terminate()

  "A GameActor" should {
    "should receive InitializePlayers message, and initialize a specified child PlayerActor references" in {
      val gameActor = sys.actorOf(Props[GameActor])
      gameActor ! InitializePlayers(2) // create 2 players, send them Play messages
      val msg = expectMsgType[Initialized]
      assertResult(2)(msg.playerActorRefs.length)
    }

    "should receive AggregatorReply and respond with Aggregated to the sender" in {
      val gameActor = sys.actorOf(Props[GameActor])
      gameActor ! AggregatorReply(Seq(AggregatedResult(1, "player1", 12345, 5), AggregatedResult(2, "player2", 11111, 10000)))
      val msg = expectMsgType[Aggregated]
      val expected = List(AggregatedResult(1, "player1", 12345, 5), AggregatedResult(2, "player2", 11111, 10000))
      assertResult(expected)(msg.results)
    }

  }

}
