import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderTest {
    OrderClient orderClient = new OrderClient();
    List<String> ingredients = OrderClient.getIngredientsIds();
    Order order = new Order(ingredients);
    User user;
    UserClient userClient;
    String accessToken;
    final List<String> WRONG_HASH = List.of("TEST");

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
        accessToken = userClient.create(user)
                .extract().path("accessToken");
    }

    @After
    public void teardown() {
        if (!accessToken.equals("")) {
            userClient.delete(user, accessToken);
        } else {
            System.out.println("Нет пользователя для удаления");
        }
    }

    @Test
    @DisplayName("Проверка создания заказа с авторизацией")
    public void createOrderWithAuthTest() {
        Response response = orderClient.createWithAuth(order, accessToken)
                .statusCode(200)
                .extract().response();

        assertTrue(response.path("success"));
    }
    @Test
    @DisplayName("Проверка создания заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        Response response = orderClient.createWithoutAuth(order)
                .statusCode(200)
                .extract().response();

        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Response response = orderClient.createWithAuth(new Order(new ArrayList<>()), accessToken)
                .statusCode(400)
                .extract().response();

        assertFalse(response.path("success"));
        assertEquals("Ingredient ids must be provided", response.path("message"));
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хэшем ингредиентов")
    public void createOrderWithWrongIngredientsHashTest() {
        orderClient.createWithAuth(new Order(WRONG_HASH), accessToken)
                .statusCode(500);
    }
}
