package com.onairentertainment.delivery.akka

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.Materializer
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import com.onairentertainment.delivery.akka.actors.WebsocketGameActor
import com.onairentertainment.delivery.akka.actors.GameActor.Aggregated
import com.onairentertainment.delivery.akka.actors.WebsocketGameActor.PlayGame
import com.onairentertainment.delivery.akka.model.{Ping, PlayPayload, Pong}
import com.onairentertainment.delivery.akka.model.json.{AggregatedResultProtocol, PingJsonProtocol, PlayPayloadJsonProtocol, PongJsonProtocol}
import spray.json._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration.DurationInt

object AkkaHttpApp extends scala.App
  with PlayPayloadJsonProtocol
  with PongJsonProtocol
  with PingJsonProtocol
  with AggregatedResultProtocol
  with SprayJsonSupport {

  implicit val system: ActorSystem          = ActorSystem("AkkaWebsocketApp")
  implicit val materializer: Materializer   = Materializer(system)
  implicit val timeout: Timeout             = Timeout(3.seconds)
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  val routes =
    path("play") { // let it be async
      get {
        handleWebSocketMessages {
          Flow[Message].mapAsync(5) {
            case TextMessage.Strict(msg) => {
              val playerCount = msg.parseJson.convertTo[PlayPayload].players
              val gameActor = system.actorOf(Props[WebsocketGameActor])

              (gameActor ? PlayGame(playerCount))
                .mapTo[Aggregated]
                .map(aggregated => TextMessage(aggregated.results.toJson.prettyPrint))
            }
            case _ => Future.successful(TextMessage("Unimplemented :)"))
          }
        }
      }
    } ~
      path("ping") {
        get { // let it be sync
          handleWebSocketMessages {
            Flow[Message].collect {
              case TextMessage.Strict(body) => {
                val ping = body.parseJson.convertTo[Ping]
                val pong = Pong(ping.id, "response.pong", ping.timestamp, System.currentTimeMillis())

                TextMessage(pong.toJson.prettyPrint)
              }
              case _ => TextMessage("Unhandled")
            }
          }
        }
      }

  Http().newServerAt("localhost", 8080).bind(routes)
  println("HTTP Server listening on port 8080")

}
