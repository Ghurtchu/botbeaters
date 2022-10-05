# Coding interview purpose

We in On Air Entertainment like to see how developers writing a code, that's why we base our interviews mostly on practical parts and code than on theoretical. 
It is expected that candidate will have a live coding interview and only some theoretical questions are expected as an addition to what is written.
Interview purpose is to demonstrate how a candidate can solve a task & how fluent candidate is with technologies used by On Air Entertainment.

**NOTE**:
Coding task is sent to the candidate in advance, so the candidate can be prepared about expectations. 
It isn't prohibited to write a part or full solution for the practical task before the interview.
It isn't expected that full solution will be written in result.

_If you started with a solution, then please publish your solution as a private repository in GitHub and grant
 access to OnAirEntertainment-Scala user before the interview._

# Technologies to use

Solution can be based (but not limited) on the following technologies:
- Scala
- Akka Actors
- Circe, Spray, Jackson or other alternative can be used for serialization
- Akka HTTP or fs2 / sttp can be used for websockets
Other options:
- Cats Effect
- ZIO or Tagless Final

# Coding task

Create an application for a Lucky Numbers game.

Game will include following minimal functionality:
- a method / service that generates a random number from 0 to 999999
- a method / service that accepts number of players, e.g. 5 and generates a random number for each player & a bot player
- a method / service that for each player & bot player constructs a game result with the following rules:
  - counts occurrences of each digit in given number, e.g. for number 447974 there are 4 - 3 times, 7 - 2 times, 9 - one time
  - calculates result for each digit by formula 10^(times-1) * digit, e.g. in number 447974 it will be 10 * 10 * 4 for 4, 10 * 7 for 7, 9 for 9  
  - summarizes all digit result, e.g. for number 447974 it will be 10 * 10 * 4 + 10 * 7 + 9 = 479
- a method / service that calculates winning list:
  - all results that are below bot player aren't included into result list
  - all winners should be sorted by position

**NOTE**:
Bot player or computer player plays against other players.

# Additional functionality 

**NOTE**: can be touched or covered during an interview

Create a websocket for managing game logic.

Following functionality should be covered by websocket:
- play message to websocket
```
{
  "message_type": "request.play",
  "players": 3
}
```
- results message from websocket
```
{
  "message_type": "response.results",
  "results": [
      {
        "position": 1, 
        "player": "1",
        "number": 966337, 
        "result": 106
      },
      {
        "position": 2, 
        "player": "3",
        "number": 964373, 
        "result": 56
      },
      {
        "position": 3, 
        "player": "2",
        "number": 4283, 
        "result": 17
      }
  ]
}
```
- ping / pong functionality (latency check)
```
{
  "id": 5,
  "message_type": "request.ping",
  "timestamp": 1234560
}
```
```
{
  "message_type": "response.pong",
  "request_id": 5,
  "request_at": 1234560,
  "timestamp": 1234567
}
```
Alternatively (but it isn't recommended) websocket can be replaced with RESTful API with `results` & `ping` requests.

# Solution for a review

You can use your own scaffolding for a project if it looks easier for you.
Please provide a short readme on how to launch and test application.
Please publish your solution as a private repository in GitHub and grant access to *OnAirEntertainment* user.
