package order;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderClient extends BaseClient {
    protected final String ORDERS = "/orders";
    protected static final String INGREDIENTS = "/ingredients";
    private static final String INGREDIENTS_PATH = "order.ingredients";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createWithAuth(Order order, String accessToken) {
        return getSpecWithAuth(accessToken)
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().all();
    }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createWithoutAuth(Order order) {
        return getSpec()
                .body(order)
                .when()
                .post(ORDERS)
                .then().log().all();
    }
//    @Step("Get ingredients from the order")
//    public List<Ingredient> getIngredientsFromOrder(String accessToken, Order body) {
//        return create(accessToken, body)
//                .extract().jsonPath().getList(INGREDIENTS_PATH, Ingredient.class);
//    }
    @Step("Get orders list. Authorized user")
    public ValidatableResponse getOrdersList(String accessToken) {
        return getSpecWithAuth(accessToken)
                .when()
                .get(ORDERS)
                .then();
    }
    @Step("Get orders list. Unauthorized user")
    public ValidatableResponse getOrdersList() {
        return getSpec()
                .when()
                .get(ORDERS)
                .then();
    }

    private static List<Ingredient> getIngredients() {
        return getSpec()
                .when()
                .get(INGREDIENTS)
                .then()
                .extract()
                .jsonPath()
                .getList("data", Ingredient.class);
    }
    @Step("Получение списка ингредиентов")
    public static List<String> getIngredientsIds() {
        List<Ingredient> ingredients = getIngredients();
        List<String> list = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            list.add(ingredient.get_id());
        }
        return list;
    }
}
