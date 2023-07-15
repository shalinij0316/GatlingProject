package com.learning.computerDatabase.checks;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class UserDemo extends Simulation {

    //http protocol
    HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://reqres.in/");
    ChainBuilder builder = exec(session -> {
        Session session1 = session.set("name", "Janet");
        return session1;
    });

    //Scenario
    ScenarioBuilder scenarioBuilder = scenario("Get list of users").exec(builder)
            .exec(http("List users").get("/api/users/2")
                    .check(
                            status().is(200),
                            status().not(500),
                            jsonPath("$.data.id").ofInt().is(2),
                            jsonPath("$.data.email").ofString().is("janet.weaver@reqres.in"),
                            regex(".*@reqres.in.*"),
                            jsonPath("$.data.email").count().saveAs("myValue"),
                            substring("#{name}"),
                            substring("Error:").notExists(),
                            substring("first_name"),
                            bodyString().is(ElFileBody("ResponseBody.json"))
                    ))
            .exec(session -> {
                        String myValue = session.get("myValue").toString();
                        System.out.println("Extracted value: " + myValue);
                        return session;
                    }
            );
    //inject
    {
        setUp(scenarioBuilder.injectOpen(atOnceUsers(1))).protocols(httpProtocolBuilder);
    }
}
