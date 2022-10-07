
The app has two solutions:
  - simple synchronous scala solution with dependency injection
  - classic untyped actor model implementation with immutable state

And three delivery choices:
  - The console (with synchronous scala solution)
  - The console (with Akka actors solution)
  - The Client/Server (with Akka HTTP Websockets solution)

How to set up things:
  - clone the repo
  - cd into ~/onair-game-simulation
  - run `test` to make sure unit and actor tests pass
  - run `run` to see the options, let's run the most interesting one (AkkaHttpApp) or you may also run AkkaConsoleApp or ScalaConsoleApp
  - in case of running AkkaHttpApp you need to start second sbt server and `run` GameWebsocketClient for making requests, pay attention to periodical sorted json responses in terminal
  - in case of running AkkaHttpApp you need to start third sbt server and run `run` PingPongWebsocketClient, pay attention to periodical json responses in terminal

Enjoy!