package com.onairentertainment.delivery.akka

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout
import com.onairentertainment.delivery.akka.actors.GameActor
import com.onairentertainment.delivery.akka.actors.GameActor.{Aggregated, InitializePlayers}
import com.onairentertainment.delivery.akka.model.{Ping, PlayPayload, Pong}
import com.onairentertainment.delivery.akka.model.json.{PingJsonProtocol, PlayPayloadJsonProtocol, PongJsonProtocol}
import spray.json._

import scala.concurrent.duration.DurationInt

object AkkaHttpApp extends scala.App
  with PlayPayloadJsonProtocol
  with PongJsonProtocol
  with PingJsonProtocol
  with SprayJsonSupport {

  implicit val system = ActorSystem("AkkaWebsocketApp")
  implicit val materializer = Materializer(system)

  val jsonResponse =
    """
      |[
      | {
      |   "position": 1,
      |   "player": "player_3",
      |   "result": 12300
      | },
      | {
      |   "position": 2,
      |   "player": "player_5",
      |   "result": 123
      | }
      |]
      |""".stripMargin

  implicit val timeout = Timeout(5.seconds)
  implicit val ec = system.dispatcher

  val route =
    path("play") {
      get {
        val amountOfPlayers = 5
        val gameActor = system.actorOf(Props[GameActor])
        val futureAggregated = (gameActor ? InitializePlayers(amountOfPlayers)).mapTo[Aggregated]
        val futureRes = futureAggregated.map { aggr =>
          HttpEntity(
            ContentTypes.`application/json`,
            jsonResponse
          )
        }
        complete(futureRes)
      }
    } ~
      path("ping") {
        get {
          handleWebSocketMessages {
            Flow[Message].collect {
              case TextMessage.Strict(msg) =>
                val ping = msg.parseJson.convertTo[Ping]
                val pong = Pong(ping.id, "response.pong", ping.timestamp, System.currentTimeMillis())
                
                TextMessage(pong.toJson.prettyPrint)
              case _ => TextMessage("Unhandled")
            }
          }
        }
      }

  Http().newServerAt("localhost", 8080).bind(route)


}
