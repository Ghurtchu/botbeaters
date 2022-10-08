package com.onairentertainment.delivery.akka.actors.ping

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.onairentertainment.delivery.akka.actors.ping.PingPongActor.{Ping, Pong}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.mutable
import scala.concurrent.duration.DurationInt

class PingPongActorTestSpec extends TestKit(ActorSystem("PingPongActorTestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll {

  val sys: ActorSystem = ActorSystem("GameActorTestSystem")

  "A PingPongActor" should {
    "receive Ping message and respond with Pong message" in {
      val pingPongActor = sys.actorOf(Props[PingPongActor])
      val now = System.currentTimeMillis()
      pingPongActor ! Ping(100, "request.ping", now)
      val expected = expectMsgType[Pong]
      assert(expected.timestamp >= now)
    }
    "not handle any other messages than Ping" in {
      val pingPongActor = sys.actorOf(Props[PingPongActor])
      pingPongActor ! mutable.HashSet
      pingPongActor ! Left("right")
      pingPongActor ! Right("left")
      expectNoMessage(3.seconds)
    }
  }

}
