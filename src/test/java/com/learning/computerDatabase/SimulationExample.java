package com.learning.computerDatabase;


import com.learning.computerDatabase.actions.ComputerDatabase;
import com.learning.computerDatabase.common.HttpProtocol;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.learning.computerDatabase.common.PerfTestConfig.P95_RESPONSE_TIME_MS;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class SimulationExample extends Simulation {

    @Override
    public void before() {
        System.out.println("Simulation is about to start!");
    }

//scenario 1
    FeederBuilder<String> feederBuilder = csv("search.csv").circular();
    private ScenarioBuilder scn1 = scenario("SearchForComputer")
            //feed will create the session based on the headers present in csv file
            // so inside your simulation you can access the session values
            .feed(feederBuilder)
            .exec(ComputerDatabase.home_Page)
            .pause(4)
            .repeat(2, "counter")
            .on(exec(ComputerDatabase.getPage("counter")))
            .exec(ComputerDatabase.searchComputer())
            .pause(2)
            .exec(ComputerDatabase.viewDetails);

        //scenario 2
    ScenarioBuilder scn2 = scenario("First way of scenario")
            .exec(http("Home").get("/computers")).pause(2);

    //scenario 3
    ScenarioBuilder scn3 = scenario("Simulation Example 1")
            .exec(http("root end point").post("/").body(StringBody("{}")));



    /*{
        setUp(
                scn3.injectOpen(rampUsers(10).during(Duration.ofMinutes(1)),
                                constantUsersPerSec(10).during(Duration.ofSeconds(10)))
                        .protocols(HttpProtocol.httpProtocol1)
        );
    }*/

   /* {
        setUp(
                scn1.injectOpen(atOnceUsers(1)),
                scn2.injectOpen(atOnceUsers(1))
        ).protocols(HttpProtocol.httpProtocol);
    }*/
   /*{
       setUp(
               scn1.injectOpen(atOnceUsers(1))
                       .protocols(HttpProtocol.httpProtocol)
       ).assertions(global().failedRequests().count().is(0L),global().successfulRequests().percent().gt(95.0)).customPauses(session -> 5L);
   }*/ /*{
       setUp(scn3.injectOpen(constantUsersPerSec(10).during(Duration.ofMinutes(1))))
               .throttle(
                       reachRps(10).in(10),
                       holdFor(Duration.ofMinutes(1)),
                       jumpToRps(5),
                       holdFor(Duration.ofMinutes(1))
               ).protocols(HttpProtocol.httpProtocol1);
   }*/ //{
      /* setUp(scn3.injectOpen(rampUsers(10).during(Duration.ofMinutes(2))))
               .maxDuration(Duration.ofMinutes(1)).protocols(HttpProtocol.httpProtocol1);;
   }*/
       {
           setUp(
                   scn1.injectOpen(atOnceUsers(1))
           ).protocols(HttpProtocol.httpProtocol);
       }

    @Override
    public void after() {
        System.out.println("Simulation is finished!");
    }

}

