package com.onairentertainment

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class WebSocketSpec extends AnyWordSpecLike with Matchers with ScalatestRouteTest with Directives {

  "WebSocket" must {
    // websocket protocol tests can be implemented here
  }

}
