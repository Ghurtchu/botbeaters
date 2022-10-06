package com.onairentertainment.delivery.akka.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.core.model.{Player, RandomNumber}
import com.onairentertainment.core.service.implementation.{OnAirResultAggregator, OnAirResultCalculator}
import com.onairentertainment.delivery.akka.actors.GameResultAggregatorActor.{AggregateResults, AggregatorReply}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.duration._

class GameResultAggregatorActorTestSpec extends TestKit(ActorSystem("GameResultAggregatorActorTestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  val sys: ActorSystem = ActorSystem("GameResultAggregatorActorTestSystem")

  override protected def afterAll(): Unit = sys.terminate()

  "A GameResultAggregatorActor" should {

    "aggregate initial results and respond with AggregatorReply(sortedResults)" in {
      val gameResultAggregatorActor = sys.actorOf(GameResultAggregatorActor.props(new OnAirResultAggregator(new OnAirResultCalculator())))
      val players = Player("player_1", Some(RandomNumber(111))) :: Player("player_2", Some(RandomNumber(123))) :: Player("player_3", Some(RandomNumber(223))) :: Nil
      gameResultAggregatorActor ! AggregateResults(players)
      val reply = expectMsgType[AggregatorReply]
      assert(reply.results.head.player == "player_1")
      assert(reply.results.tail.head.player == "player_3")
      assert(reply.results.tail.tail.head.player == "player_2")
    }

    "only respond with AggregatorReply" in {
      val gameResultAggregatorActor = sys.actorOf(GameResultAggregatorActor.props(new OnAirResultAggregator(new OnAirResultCalculator())))
      gameResultAggregatorActor ! AggregateResults(Nil)
      expectMsgType[AggregatorReply]
    }

    "not handle any other messages than AggregateResults" in {
      val gameResultAggregatorActor = sys.actorOf(GameResultAggregatorActor.props(new OnAirResultAggregator(new OnAirResultCalculator())))
      gameResultAggregatorActor ! 5
      gameResultAggregatorActor ! true
      gameResultAggregatorActor ! IndexedSeq()
      gameResultAggregatorActor ! "Akka rocks!"
      expectNoMessage(3.seconds)
    }

  }

}
