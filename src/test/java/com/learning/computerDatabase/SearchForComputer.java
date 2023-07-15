package com.learning.computerDatabase;

import static io.gatling.javaapi.core.CoreDsl.*;

import com.learning.computerDatabase.actions.ComputerDatabase;
import com.learning.computerDatabase.common.HttpProtocol;
import io.gatling.javaapi.core.*;

public class SearchForComputer extends Simulation {
    FeederBuilder<String> feederBuilder=csv("search.csv").circular();

    private ScenarioBuilder scn = scenario("SearchForComputer")
            //feed will create the session based on the headers present in csv file
            // so inside your simulation you can access the session values
            .feed(feederBuilder)
            .exec(ComputerDatabase.home_Page)
            .pause(4)
            .repeat(2,"counter")
            .on(exec(ComputerDatabase.getPage("counter")))
//            .exec(ComputerDatabase.page1)
//            .pause(3)
//            .exec(ComputerDatabase.page2)
//            .pause(7)
            .exec(ComputerDatabase.searchComputer())
            .pause(2)
            .exec(ComputerDatabase.viewDetails);

    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(HttpProtocol.httpProtocol);
    }
}

