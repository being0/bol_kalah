# Kalah game API

**[Solution](#heading-solution)**

* [Create game](#heading-solution-1)
* [Make a move](#heading-solution-2)

**[How to run](#heading-run)**

**[How to test](#heading-test)**

**[How to scale](#heading-scale)**

* [App servers](#heading-scale-1)
* [Database](#heading-scale-2)

**[TODO](#heading-todo)**


<div id="heading-solution"/>

## Solution

To implement Kalah game, two REST APIs have been developed. The first end point creates the game and the second end
point allows player to play the game.

<div id="heading-solution-1"/>

### Create Game

```
POST
http://<host>:<port>/games
```

This endpoint creates a new Kalah game.

#### Response Body

```json
{
  "id": "1b3753ef-fda4-4ce9-9585-1f2737c09106",
  "status": {
    "1": "6",
    "2": "6",
    "3": "6",
    "4": "6",
    "5": "6",
    "6": "6",
    "7": "0",
    "8": "6",
    "9": "6",
    "10": "6",
    "11": "6",
    "12": "6",
    "13": "6",
    "14": "0"
  }
}
```

#### Response Codes

| HTTP Code             | Explanation             |
| -------------         |:-----------------------------------------------:|
| 201 (Created)         | The game created |


<div id="heading-solution-2"/>

### Make a move

```
PUT
http://<host>:<port>/games/<gameId>/pits/<pitId>
```

This endpoint makes a move on the kalah board by specifying gameId and pitId.

#### Response Body

```json
{
  "id": "1b3753ef-fda4-4ce9-9585-1f2737c09106",
  "status": {
    "1": "6",
    "2": "0",
    "3": "7",
    "4": "7",
    "5": "7",
    "6": "7",
    "7": "1",
    "8": "7",
    "9": "6",
    "10": "6",
    "11": "6",
    "12": "6",
    "13": "6",
    "14": "0"
  },
  "turn": "PLAYER2"
}
```

#### Responses

| HTTP Code                 | Explanation             |
| -------------             |:-----------------------------------------------:|
| 200 (OK)                  | Success move |
| 400 (Bad request)         | If pitId is less than 1 or larger than 14    |
| 409 (Conflict)            | Game is finished/Move home stones/Pit is empty/Not this user turn      |
| 404 (Not found)           | Game not found      |


<div id="heading-run"/>

## How to run

First run Mongodb using docker compose:
    
    $ ./docker-compose up

Then run the application using gradle:

    $ ./gradlew bootRun

Good job! Your application should be listening on port of 8080.

<div id="heading-test"/>

## How to test

For simplicity all APIs are accessible publicly, so no Authentication is required.

Here is how you can create a game:

    curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/games

And then use Move API with the gameId in response and your selected pitId.
For example:

    curl -X PUT -H 'Content-Type: application/json' -i http://localhost:8080/games/ff28151d-657b-45b2-89ac-34b42ffc345a/pits/2

<div id="heading-scale"/>

## How to scale

<div id="heading-scale-1"/>

### App Servers

By Adding a load balancer behind the app servers and adding more machines, the app servers can be easily
scaled, there is no bottleneck in the app servers.

<div id="heading-scale-2"/>

### Database

By growing the load we should Partition the database. 

<div id="heading-todo"/>

## TODO

* **Monitoring**

  We should monitor response time, thread pool status, components that work with external tools(DB),... 
  and raise an alert when critical situation happens. **Micrometer** is a popular library in Spring for this purpose.

* **Security**

  The APIs need Authentication. The Kalah application could be an oauth2 resource server. 
  The user who creates the game could be the owner of the game and can set the opponent user.
  Opponent user can receive an invitation and join the game.

* **Shorter ID**
  
  UUID used for its simplicity, but it is too large. We need to create a shorter id.
  The id generator can use unique_server_Id+timestamp+large_random_id and convert it to base32/64(or anything else)

* **Swagger**