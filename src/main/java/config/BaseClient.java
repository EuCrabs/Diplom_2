package config;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClient {
    protected static RequestSpecification getSpec() {
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL);
    }

    protected RequestSpecification getSpecWithAuth(String accessToken) {
        return given().log().all()
                .headers( "Authorization", accessToken, "Content-type", "application/json")
                .baseUri(Config.BASE_URL);
    }
}
