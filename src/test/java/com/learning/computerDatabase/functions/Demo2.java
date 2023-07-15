package com.learning.computerDatabase.functions;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.util.*;
import java.util.function.Function;

import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.core.CoreDsl.*;


public class Demo2 extends Simulation {


    Function<Session, String> username = session -> "user_" + String.valueOf(new Random().nextInt(100));
    Function<Session, String> password = session -> "pass_" + String.valueOf(new Random().nextInt(100));
    Function<Session, String> id = session -> String.valueOf(new Random().nextInt(100));

    ChainBuilder chainBuilder = exec(
            session -> {
                String appendSingleQuote = "\"";
                String username1 = "\"username\" :" + appendSingleQuote + username.apply(session) + appendSingleQuote;
                String password1 = "\n\"password\" :" + appendSingleQuote + password.apply(session) + appendSingleQuote;
                String id1 = "\n\"id\" :" + appendSingleQuote + id.apply(session) + appendSingleQuote;

                String payload = "{" + username1 + "," + password1 + "," + id1 + "}";
                System.out.println("********* " + payload.toString());
                Session session1 = session.set("jsonPayload", payload);
                return session1;
            }

    );


    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://fakerestapi.azurewebsites.net");

    ScenarioBuilder scenarioBuilder = scenario("Create Users")
            .exec(chainBuilder)
            .exec()
            .exec(http("Post User")
                    .post("/api/v1/Authors")
                    .header("accept", "text/plain")
                    .header("Content-Type", "application/json")
                    .body(StringBody("#{jsonPayload}")).asJson())
            .exec(session -> {
                System.out.println("******* " + session);
                return session;
            });

    {
        setUp(
                scenarioBuilder.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
