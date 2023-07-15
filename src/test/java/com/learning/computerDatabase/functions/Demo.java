package com.learning.computerDatabase.functions;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.*;


public class Demo extends Simulation {
    HttpProtocolBuilder protocolBuilder = http.baseUrl("https://reqres.in");
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    ChainBuilder builder = exec(session -> {
        Session session1 = session.set("testData", list);
        return session1;
    });
    Function<Session, String> getUserId = session -> "/api/users/" +
            session.getList("testData").get((int) session.userId());
    ScenarioBuilder scenarioBuilder = scenario("Single User").exec(builder)
            .exec(http("Get Single User").get(getUserId));

    {
        setUp(
                scenarioBuilder.injectOpen(atOnceUsers(1))
        ).protocols(protocolBuilder);
    }
}


