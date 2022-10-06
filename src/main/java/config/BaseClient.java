package config;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClient {
    protected RequestSpecification getSpec() {
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL);
    }
}
