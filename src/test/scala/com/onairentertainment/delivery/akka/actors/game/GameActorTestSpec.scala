package com.onairentertainment.delivery.akka.actors.game

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.delivery.akka.actors.game.GameActor._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class GameActorTestSpec extends TestKit(ActorSystem("GameResultAggregatorActorTestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  val sys: ActorSystem = ActorSystem("GameActorTestSystem")

  override protected def afterAll(): Unit = sys.terminate()

  "A GameActor" should {
    "should receive InitializeGame and respond with GameResult to the sender" in {
      val gameActor = sys.actorOf(Props[GameActor])
      gameActor ! InitializeGame(5)

      expectMsgType[GameResult]
    }
    "should respond with sorted results" in {
      val gameActor = sys.actorOf(Props[GameActor])
      gameActor ! InitializeGame(5)
      val actual = expectMsgType[GameResult]

      assertResult(actual.results.sortWith(_.result > _.result))(actual.results)
    }
  }
}
