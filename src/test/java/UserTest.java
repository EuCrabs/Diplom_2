import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.junit.Assert.assertTrue;

public class UserTest {
    User user;
    UserClient userClient;

    @Before
    public void setup() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        // add user deletion here
    }

    @Test
    @DisplayName("Проверка создания уникального юзера")
    public void createUniqueUser() {
        boolean isUserCreated = userClient.create(user)
                .statusCode(200)
                .extract().path("success");

        assertTrue(isUserCreated);
    }
}
