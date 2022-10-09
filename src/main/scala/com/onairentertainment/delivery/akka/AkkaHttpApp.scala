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
import com.onairentertainment.delivery.akka.actors.game.GameActor
import com.onairentertainment.delivery.akka.actors.game.GameActor.{GameResult, InitializeGame}
import com.onairentertainment.delivery.akka.actors.ping.PingPongActor
import com.onairentertainment.delivery.akka.actors.ping.PingPongActor.{Ping, Pong}
import com.onairentertainment.delivery.akka.model.PlayPayload
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

  // let it be async
  val routes =
    path("play") {
      get {
        handleWebSocketMessages {
          Flow[Message].mapAsync(5) {
            case TextMessage.Strict(body) => {
              val playerCount = body.parseJson.convertTo[PlayPayload].players
              val gameActor = system.actorOf(Props[GameActor])
              val futureGameResultMsg = (gameActor ? InitializeGame(playerCount))
                .mapTo[GameResult]
                .map(_.results.toMsg)

              futureGameResultMsg
            }
            case _ => helloFromFuture
          }
        }
      }
    } ~
      path("ping") {
        get {
          handleWebSocketMessages {
            Flow[Message].mapAsync(5) {
              case TextMessage.Strict(body) => {
                val ping = body.parseJson.convertTo[Ping]
                val pingPongActor = system.actorOf(Props[PingPongActor])
                val futurePongMsg = (pingPongActor ? ping)
                  .mapTo[Pong]
                  .map(_.toMsg)

                futurePongMsg
              }
              case _ => helloFromFuture
            }
          }
        }
      }

  private final implicit class EntityToMessageConverter[A: JsonWriter](entity: A) {
      def toMsg: Message = TextMessage(entity.toJson.prettyPrint)
  }

  private def helloFromFuture = Future.successful(TextMessage("Hello from Future!"))

  Http().newServerAt("localhost", 8080).bind(routes)
  println("HTTP Server listening on port 8080")

}
