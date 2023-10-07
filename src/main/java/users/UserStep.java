package users;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static constant.BaseUrl.*;
import static constant.Endpoints.*;

import static io.restassured.RestAssured.given;

public class UserStep {
    @Step("Регистрация пользователя")
    public static ValidatableResponse createUser(UserData user) {
        return given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(REGISTER_API)
                .then();
    }

    @Step("Авторизация пользователем")
    public static ValidatableResponse loginUser(UserData user) {
        return given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .post(LOGIN_API)
                .then();
    }
    @Step("Удаление пользователя")
    public static ValidatableResponse deleteUser(UserData user, String token) {
        return given()
                .spec(requestSpecificationAuth(token))
                .body(user)
                .when()
                .delete(USER_API)
                .then();
    }
    @Step("Изменения  пользователя с авторизацией")
    public static ValidatableResponse changeDataWithAuth(UserNewData user, String token) {
        return given()
                .spec(requestSpecificationAuth(token))
                .body(user)
                .when()
                .patch(USER_API)
                .then();
    }

    @Step("Изменение пользователя без авторизации")
    public static ValidatableResponse changeDataWithoutAuth(UserNewData user) {
        return given()
                .spec(requestSpecification())
                .body(user)
                .when()
                .patch(USER_API)
                .then();
    }
}
