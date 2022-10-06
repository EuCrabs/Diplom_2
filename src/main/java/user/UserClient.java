package user;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserClient extends BaseClient {
    private final String REGISTER = "/auth/register";
    private final String USER = "/auth/user";
    private final String LOGIN = "/auth/login";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all();
    }

    // нужно понять как передавать поле
    @Step("Изменение пользователя")
    public ValidatableResponse uodate(User user, String accessToken) {
        return getSpecWithAuth(accessToken)
                .body(user)
                .when()
                .patch(USER)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public void delete(User user, String accessToken) {
        getSpecWithAuth(accessToken)
                .body(user)
                .when()
                .delete(USER)
                .then().log().all();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(UserCredentials userCredentials, String accessToken) {
        return getSpecWithAuth(accessToken)
                .body(userCredentials)
                .when()
                .post(LOGIN)
                .then().log().all();
    }
}
