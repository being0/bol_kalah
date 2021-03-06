# Kalah game API

**[Solution](#heading-solution)**

* [Create game](#heading-solution-1)
* [Make a move](#heading-solution-2)

**[How to run](#heading-run)**

**[How to test](#heading-test)**

**[How to scale](#heading-scale)**

* [App servers](#heading-scale-1)
* [Database](#heading-scale-2)

**[Components](#heading-components)**

**[TODO](#heading-todo)**


<div id="heading-solution"/>

## Solution

To implement Kalah game, two REST APIs have been developed. The first end point creates the game and the second end
point allows players to move the game forward.

<div id="heading-solution-1"/>

### Create Game

This endpoint creates a new Kalah game and returns its id and the board status.

```
POST
http://<host>:<port>/games
```

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

This endpoint makes a move on the kalah board by specifying gameId and pitId.

```
PUT
http://<host>:<port>/games/<gameId>/pits/<pitId>
```

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
| 409 (Conflict)            | Game is finished/Moving home stones/Pit is empty/Not the user turn      |
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


<div id="heading-components"/>

## Components

To design the app server the following components have been developed:

* **KalahController**

  KalahController provides REST APIs on POST:/games and PUT:/games/{gameId}/pits/{pitId} and GET:/games/{gameId}

* **Kalah**
  
  Kalah is domain model of Kalah game application.

* **KalahTo and KalahGameMapper**

  KalahTo is the DTO for Kalah domain and KalahGameMapper maps Kalah to KalahTo

* **KalahService**

  KalahService provide main logic of the application using Mongo repository and KalahStateEngine components. 
  Moves are done by applying [**Optimistic Locking**](https://en.wikipedia.org/wiki/Optimistic_concurrency_control) 
  to prevent rare scenarios that concurrency or double submit can cause issues.

* **KalahStateEngine**

  KalahStateEngine provides the logic to move the game forward. TwoPlayersKalahStateEngine has been implemented for this purpose.
 
* **IdGenerator**

  This component is used by KalahService to generate unique id.
  For simplicity UuidIdGenerator implemented that returns UUID as uniqueId. 
  Since UUID is too large it is better to generate a smaller unique id.  

* **Using Groovy/Spock for test**

  Groovy/Spock provides a lot of benefits for testing:
    * Spock code is readable and self explaining, you can write your specification on the method names as it is in the documentation. Junit also has \@DisplayName but developers are lazy :D.
    * Spock overloads operators to simplify asserts
    * Enforcing BDD
    * Much better Parameterized Testing than Junit. See TwoPlayersKalahStateEngineSpec and you get my point.

<div id="heading-todo"/>

## TODO

* **Monitoring**

  We should monitor response time, thread pool status, components that work with external tools(DB),... 
  and raise an alert when critical situation happens. **Micrometer** is a popular library in Spring for this purpose.

* **Security**

  The APIs need Authentication. The Kalah application could be an oauth2 resource server. 

* **Shorter ID**
  
  UUID used for its simplicity, but it is too large. We need to create a shorter id.
  The id generator can use unique_server_Id+timestamp(from now to 100 years)+large_random_id and convert it to base32/64(or anything else)

* **Keep movement history**

* **Code Quality**

  I tried to add this project to sonarcloud.io, but since it doesn't support Groovy, 
  I couldn't get code coverage there(code smell is 0 and coverage using IntelijIdea is 90%).

* **Swagger**