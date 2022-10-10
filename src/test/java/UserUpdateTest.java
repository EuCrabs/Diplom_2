import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.junit.Assert.*;

public class UserUpdateTest {
    User user;
    UserClient userClient;
    UserCredentials userCredentials = new UserCredentials();
    String accessToken;
    String invalidField = "blablalba";
    String authError = "You should be authorised";

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
    @DisplayName("Изменение email с авторизацией")
    public void patchEmailWithAuth() {
        userCredentials.setEmail(invalidField);
        Response response = userClient.updateWithAuth(userCredentials, accessToken)
                .statusCode(200)
                .extract().response();

        assertTrue(response.path("success"));
        assertEquals(userCredentials.getEmail(), response.path("user.email"));
    }

    @Test
    @DisplayName("Изменение name с авторизацией")
    public void patchNameWithAuth() {
        userCredentials.setName(invalidField);
        Response response = userClient.updateWithAuth(userCredentials, accessToken)
                .statusCode(200)
                .extract().response();

        assertTrue(response.path("success"));
        assertEquals(userCredentials.getName(), response.path("user.name"));
    }

    @Test
    @DisplayName("Изменение password с авторизацией")
    public void patchPasswordWithAuth() {
        userCredentials.setPassword(invalidField);
        Response response = userClient.updateWithAuth(userCredentials, accessToken)
                .statusCode(200)
                .extract().response();

        assertTrue(response.path("success"));
    }

    @Test
    @DisplayName("Изменение email без авторизацией")
    public void patchEmailWithoutAuth() {
        userCredentials.setEmail(invalidField);
        Response response = userClient.updateWithoutAuth(userCredentials)
                .statusCode(401)
                .extract().response();

        accessToken = "";

        assertFalse(response.path("success"));
        assertEquals(authError, response.path("message"));
    }

    @Test
    @DisplayName("Изменение name без авторизацией")
    public void patchNameWithoutAuth() {
        userCredentials.setName(invalidField);
        Response response = userClient.updateWithoutAuth(userCredentials)
                .statusCode(401)
                .extract().response();

        accessToken = "";

        assertFalse(response.path("success"));
        assertEquals(authError, response.path("message"));
    }

    @Test
    @DisplayName("Изменение password без авторизацией")
    public void patchPasswordWithoutAuth() {
        userCredentials.setPassword(invalidField);
        Response response = userClient.updateWithoutAuth(userCredentials)
                .statusCode(401)
                .extract().response();

        accessToken = "";

        assertFalse(response.path("success"));
        assertEquals(authError, response.path("message"));
    }
}
