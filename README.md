The game idea is based on RNG and is really simple:

The client sends the message to the server where it specifies the amount of players for each round. The server uses Akka actors for intercommunication. The game engine is simple:
- The engine generates a random number for each player and the bot
- it then applies some business logic and calculates the result for each player
- then drops all players who were outperformed by bot
- and finally sorts the results in descending order.

Let's see the flow:

- The client sends a simple JSON via WebSocket to initialize the game
  
  The JSON looks like this:
  ```json
    {
      "messageType": "request.play",
      "players": 10
    }
  ```
  
- The server uses Akka Actors for intercommunication which looks like this:
  - Server receives the JSON, parses it, initializes the `GameActor` and sends `InitializeGame` to it.
  - `GameActor` spawns N amount of `PlayerActor`-s and sends `Play` message to each.
  - each `PlayerActor` sends `GenerateRandomNumber` to `RandomNumberGeneratorActor`.
  - `RandomNumberGeneratorActor` generates the random number and replies back to `PlayerActor` with `PlayerUpdated` message.
  - `PlayerActor` then replies `GameActor` with `PlayerReply` message.
  - Once all messages received, `GameActor` creates `GameResultAggregatorActor` and sends `AggregateResults` message to it.
  - `GameResultAggregatorActor` applies the business logic to the game and responds back with `GameAggregatorReply`.
  - `GameActor` responds back with the `GameResult` message which holds the final outcome for the round.
  - And finally the result gets parsed and delivered as a WebSocket response.

The typical response looks like:

```json
[
  {
    "number": 100400,
    "player": "player_82d7f47f-84b1-48c0-a026-635d87020abe",
    "position": 1,
    "result": 1002
  },
  {
    "number": 616186,
    "player": "player_b5811f05-9884-470c-b4d4-6b389fb513f8",
    "position": 2,
    "result": 111
  },
  {
    "number": 542540,
    "player": "player_b700dec7-751f-406a-86a3-5ef1c72c2eea",
    "position": 3,
    "result": 22
  },
  {
    "number": 325584,
    "player": "player_874b306a-584a-4f7e-b7f1-81ce1a67b322",
    "position": 4,
    "result": 14
  },
  {
    "number": 691235,
    "player": "player_82d7f47f-84b1-48c0-a026-635d87020abe",
    "position": 5,
    "result": 6
  }
]
```

Setup instructions:
  - clone the repo
  - cd into ~/onair-game-simulation
  - run `test` to make sure unit and actor tests pass
  - run `run` to see the options, let's run the most interesting one (AkkaHttpApp) or you may also run AkkaConsoleApp or ScalaConsoleApp
  - in case of running AkkaHttpApp you need to start second sbt server and `run` GameWebsocketClient for making randomized requests, please pay attention to periodical sorted json responses in terminal
  - in case of running AkkaHttpApp you need to start third sbt server and run `run` PingPongWebsocketClient for making randomized requests, please pay attention to periodical json responses in terminal

The app can also be very simply set up with the help of IntelliJ IDEA.

Enjoy!
