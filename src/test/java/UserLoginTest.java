import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.junit.Assert.*;

public class UserLoginTest {
    User user;
    UserClient userClient;
    UserCredentials userCredentials;
    String accessToken;

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
    @DisplayName("Проверка логина под существующим пользователем")
    public void validLogin() {
        userCredentials = UserCredentials.from(user);
        Response response = userClient.login(userCredentials, accessToken)
                .statusCode(200)
                .extract().response();

        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Проверка логина с неверным логином и паролем")
    public void loginWithInvalidPassword() {
        user.setPassword("blablabla");
        userCredentials = UserCredentials.from(user);
        Response response = userClient.login(userCredentials, accessToken)
                .statusCode(401)
                .extract().response();

        assertFalse(response.path("success"));
        assertEquals("email or password are incorrect", response.path("message"));
    }
}
