package com.learning.computerDatabase.session;


import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.Arrays;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
public class SessionsDemo extends Simulation {

    HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://videogamedb.uk:443/api")
            .acceptHeader("application/json");

    //Printing a session
    ScenarioBuilder scn1 = scenario("scenario 1")
            .exec(http("Get all games")
                    .get("/videogame"))
            .exec(session -> {
                System.out.println(session);
                return session;
            });

    //Injecting data using check saveAS
    ScenarioBuilder scn2 = scenario("scenario 2")
            .exec(http("Getting all games")
                    .get("/videogame").check((status().is(200))).check(jsonPath("$[0].id").saveAs("gameId")))
            .exec(http("Get specific game using id").get("/videogame/#{gameId}").check(status().is(200)));


    //Injecting data using session.set method
    List<Integer> list = Arrays.asList(1,3,5,6);
    ChainBuilder session1 = exec(session -> {
        Session newSession = session.set("gameIdlist",list); //sending the list of integers as object
        return newSession;
    });

    //getting the gameId from session and using it in the below request
    ScenarioBuilder scn3 = scenario("scenario 3").exec(session1)
            .exec(http("Get specific game")
                    .get("/videogame/#{gameIdlist(2)}")
                    .check(status().is(200)));

    //Session State Handling
    ScenarioBuilder scn4 = scenario("scenario 4")
            .exec(http("Session Manipulation")
                    .get("/videogame").check(status().is(200)))
            .exec(session -> {
                Session session2 = session.markAsFailed();
                return session2;
            })
            .exitHereIfFailed()
            .exec(http("request after session failure").get("/videogame"));

    //Using built-in methods of Expression language
    ScenarioBuilder scn5 = scenario("scenario 5")
            .exec(http("Get specific game with random id")
                    .get("/videogame/#{randomInt(1,10)}")
                    .check(status().is(200)));
    {
        setUp(scn1.injectOpen(atOnceUsers(1)),
                scn2.injectOpen(atOnceUsers(1)),
                scn3.injectOpen(atOnceUsers(1)),
                scn4.injectOpen(atOnceUsers(1)),
                scn5.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocolBuilder);
    }
}

