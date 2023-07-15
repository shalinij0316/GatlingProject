package com.learning.computerDatabase.actions;

import com.learning.computerDatabase.common.Headers;
import io.gatling.javaapi.core.ActionBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class ComputerDatabase {


    public static ActionBuilder home_Page = http("Home Page")
            .get("/computers")
            .headers(Headers.headers_0);
    public static ActionBuilder page1 = http("Page one")
            .get("/computers?p=1")
            .headers(Headers.headers_1);
    public static ActionBuilder page2 = http("Page two")
            .get("/computers?p=2")
            .headers(Headers.headers_1);
    public static ActionBuilder search = http("Search with computer name")
            .get("/computers?f=dell")
            .headers(Headers.headers_1);
    public static ActionBuilder viewDetails = http("View Computer details")
            .get("/computers/567")
            .headers(Headers.headers_1);

    public static ActionBuilder getPage(String pageNumber) {
        return http("Page #{" + pageNumber + "}")
                .get("/computers?p=#{" + pageNumber + "}")
                .headers(Headers.headers_1);
    }

    public static ActionBuilder searchComputer() {
        return http("Search with computer name")
                .get("/computers?f=#{feederBuilder}")
                .headers(Headers.headers_1);
    }

    public static ActionBuilder addComputer(){
        return  http("Adding a new computer #{name} ").post("/computers").formParam("name","#{name}").formParam("introduced","#{introduced}").formParam("discontinued","#{discontinued}").formParam("company","#{company}").headers(Headers.headers_1);
    }

}
