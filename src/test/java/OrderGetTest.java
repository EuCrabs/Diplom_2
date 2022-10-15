import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Order;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OrderGetTest {
    OrderClient orderClient = new OrderClient();
    Order order = new Order(OrderClient.getIngredientsIds());
    User user;
    String accessToken;
    UserClient userClient = new UserClient();

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
    @DisplayName("Проверка получение заказов конкретного авторизированного пользователя")
    public void getOrderWithAuthTest() {
        orderClient.createWithAuth(order, accessToken);

        Response response = orderClient.getOrdersList(accessToken)
                .statusCode(200)
                .extract().response();

        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Проверка получение заказов конкретного неавторизированного пользователя")
    public void getOrderWithoutAuthTest() {
        orderClient.createWithAuth(order, accessToken);

        Response response = orderClient.getOrdersList()
                .statusCode(401)
                .extract().response();

        assertFalse(response.path("success"));
    }
}
