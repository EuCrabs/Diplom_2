package order;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;

public class OrderClient extends BaseClient {
    private final String GET_INGREDIENTS = "/ingredients";

    @Step("Получение списка ингредиентов")
    public ArrayList<String> getIngredient() {
        return getSpec()
                .when()
                .get(GET_INGREDIENTS)
                .then().log().all()
                .extract().path("_id");
    }
}
