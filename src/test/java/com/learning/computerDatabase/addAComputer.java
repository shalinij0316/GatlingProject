package com.learning.computerDatabase;

import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;

import com.learning.computerDatabase.actions.ComputerDatabase;
import com.learning.computerDatabase.common.HttpProtocol;
import io.gatling.javaapi.core.*;

public class addAComputer extends Simulation {


//FeederBuilder<String> addComputerFeeder=csv("data/addComputerTestData.csv").circular();


        FeederBuilder.FileBased<Object> addComputerFeeder=jsonFile("data/addComputerTestData.json").circular();


    private ScenarioBuilder scn = scenario("Add Computers")
            .feed(addComputerFeeder)
            .exec(ComputerDatabase.addComputer())
            .pause(4);

    {
        setUp(scn.injectOpen(atOnceUsers(2))).protocols(HttpProtocol.httpProtocol);
    }


}
