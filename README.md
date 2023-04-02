
The app has two solutions:
  - simple synchronous scala solution with plain dependency injection
  - classic untyped actor model implementation with immutable state

And three delivery choices:
  - The console (synchronous Scala)
  - The console (Akka actors)
  - The Client/Server (with Akka HTTP WebSockets)

The main classes in the app are: 
  - ScalaConsoleApp
  - AkkaConsoleApp
  - AkkaHttpApp
  - GameWebsocketClient
  - PingPongWebsocketClient

Setup instructions:
  - clone the repo
  - cd into ~/onair-game-simulation
  - run `test` to make sure unit and actor tests pass
  - run `run` to see the options, let's run the most interesting one (AkkaHttpApp) or you may also run AkkaConsoleApp or ScalaConsoleApp
  - in case of running AkkaHttpApp you need to start second sbt server and `run` GameWebsocketClient for making randomized requests, please pay attention to periodical sorted json responses in terminal
  - in case of running AkkaHttpApp you need to start third sbt server and run `run` PingPongWebsocketClient for making randomized requests, please pay attention to periodical json responses in terminal
  
Typical JSON response looks like:
"player_e885518e-131f-4982-bd40-3c5031a956b1"

The app can also be very simply set up with the help of IntelliJ IDEA.

Enjoy!
