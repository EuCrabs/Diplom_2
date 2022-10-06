package user;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserClient extends BaseClient {
    private final String REGISTER = "/auth/register";
    private final String DELETE = "/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public void delete(User user, String accessToken) {
        getSpecWithAuth(accessToken)
                .body(user)
                .when()
                .delete(DELETE)
                .then().log().all();
    }
}
