import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.junit.Assert.*;

public class UserTest {
    User user;
    UserClient userClient;
    String accessToken;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
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
    @DisplayName("Проверка создания уникального пользователя")
    public void createUniqueUser() {
        Response response = userClient.create(user)
                .statusCode(200)
                .extract().response();

        accessToken = response.path("accessToken");

        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    public void createNonUniqueUser() {
        Response initResponse = userClient.create(user)
                .extract().response();

        accessToken = initResponse.path("accessToken");

        Response response = userClient.create(user)
                .statusCode(403)
                .extract().response();

        assertFalse(response.path("success"));
        assertEquals("User already exists", response.path("message"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без заполненного одно из обязательных полей - name")
    public void createUserWithEmptyName() {
        user.setName("");

        Response response = userClient.create(user)
                .statusCode(403)
                .extract().response();

        accessToken = "";

        assertFalse(response.path("success"));
        assertEquals("Email, password and name are required fields", response.path("message"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без заполненного одно из обязательных полей - password")
    public void createUserWithEmptyPassword() {
        user.setPassword("");

        Response response = userClient.create(user)
                .statusCode(403)
                .extract().response();

        accessToken = "";

        assertFalse(response.path("success"));
        assertEquals("Email, password and name are required fields", response.path("message"));
    }
}
