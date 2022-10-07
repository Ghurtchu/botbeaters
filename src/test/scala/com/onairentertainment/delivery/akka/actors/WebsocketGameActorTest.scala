package com.onairentertainment.delivery.akka.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.delivery.akka.actors.GameActor.Aggregated
import com.onairentertainment.delivery.akka.actors.WebsocketGameActor.PlayGame
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class WebsocketGameActorTest extends TestKit(ActorSystem("PlayerActorTestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  override protected def afterAll(): Unit = system.terminate()

  "A WebsocketGameActor" should {
    "receive PlayGame(n) message and respond with Aggregated result with sorted values in it" in {
      val websocketGameActor = system.actorOf(Props[WebsocketGameActor])
      websocketGameActor ! PlayGame(5)
      val msg = expectMsgType[Aggregated]
      assertResult(msg.results.sortWith(_.result > _.result))(msg.results)
    }
  }

}
