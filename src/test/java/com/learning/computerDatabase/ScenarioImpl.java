package com.learning.computerDatabase;

import com.learning.computerDatabase.common.HttpProtocol;
import io.gatling.core.scenario.Scenario;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.core.Session;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ScenarioImpl extends Simulation {

    /*
    multiple ways to create a scenario
    */


    //using single exec statement
    ScenarioBuilder firstWay = scenario("First way of scenario")
            .exec(http("Home").get("/computers")).pause(2);

    //using chain builder
    ChainBuilder cb = exec(http("home page").get("/computers")).pause(2);

    //using chained exec statements into single scenario
    ScenarioBuilder sb = scenario("user journey")
            .exec(http("home").get("/computers")).pause(Duration.ofMillis(100))
            .exec(http("search").get("/computers?f=ACE")).pause("#{var}")
            .exec(http("details").get("/computers/381"));


    //different types of pauses methods
    ScenarioBuilder pausesfun = scenario("user journey")
            .exec(http("home").get("/computers")).pause(2)
            .exec(http("search").get("/computers?f=ACE")).pause(2,10)
            .exec(http("details").get("/computers/381")).pause(Duration.ofMillis(100))
            .exec(http("search").get("/computers/382")).pause("#{var}");



    //using foreach loop in our scripts
    List<String> viewComputersLst = Arrays.asList(
            "/computers/381",
            "/computers/76",
            "/computers/382",
            "/computers/313"
    );

    ScenarioBuilder foreachExamples = scenario("My Scenario with foreach")
      .foreach(viewComputersLst, "url", "i").on(
        exec(http("requests #{url} ").get("/#{url}"))
            );

    //during method
     ChainBuilder during_eg = during(3).on(exec(http("only1search_during").get("/computers?f=ACE")));


     //use of "doIf"
     // do if set session variable times to true
     ChainBuilder sessVar1 = exec(session -> {
         Session newSession = session.set("isValidSession", false);
         return newSession;
     });

     ScenarioBuilder doif = scenario("do if test").exec(sessVar1)
             .doIf("#{isValidSession}").then(
                exec(http("If session is valid then search").get("/computers?f=ACE")).pause(2)
             );

     // using doIfEqualsOrElse
    ScenarioBuilder doifelse = scenario("do if test").exec(sessVar1)
            .doIfEqualsOrElse("#{isValidSession}", true).then(
            // executed if the session value stored in "actual" is equal to "expectedValue"
                    exec(http("Select ").get("/computers/381"))
            ).orElse(
                    exec(http("Search ").get("/computers?f=ACE"))
            );


    //Error Handling
    ScenarioBuilder trymax = scenario("do if test")
            .tryMax(5,"i").on(
                    exec(http("Select #{i}").get("computers/381"))
            );


    ScenarioBuilder doIfExamples = scenario("My Scenario with foreach").exec(sessVar1)
            .doIf("#{isValidSession}").then(
                    tryMax(2).on(
                    exec(http("Select ").get("computers/381"))
                    )
            );




    //using Groups

    ScenarioBuilder forEachEg = scenario("user journey")
            .group("view computer group").on(
                    exec(http("details 381").get("/computers/381")).pause(2)
                            .exec(http("details 76").get("/computers/76")).pause(2)
                            .exec(http("details 382").get("/computers/382")).pause(2)
            ).group("search computer group").on(
                    exec(http("search 1").get("/computers?f=ACE")).pause(2)
                            .exec(http("search computer 2").get("/computers?f=ARRA")).pause(1)
                            .exec(http("search computer 3").get("/computers?f=APEXC")).pause(1)
            );







    //ScenarioBuilder sb = scenario("Entire Path").exec(cb,c1,c2);


    {
        setUp(sb.injectOpen(atOnceUsers(1))).protocols(HttpProtocol.httpProtocol);
    }
}
