package orders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static constant.BaseUrl.*;
import static constant.Endpoints.*;

import static io.restassured.RestAssured.given;

public class OrderStep {

    @Step("Получение данных об ингредиентах")
    public static ValidatableResponse getIngredients() {
        return given()
                .spec(requestSpecification())
                .when()
                .get(INGREDIENTS_API)
                .then();
    }

    @Step("Создание нового заказа с авторизацией")
    public static ValidatableResponse createOrderWithAuth(String token, OrderData order) {
        return given()
                .spec(requestSpecificationAuth(token))
                .body(order)
                .when()
                .post(ORDERS_API)
                .then();
    }

    @Step("Создание нового заказа без авторизации")
    public static ValidatableResponse createOrderWithoutAuth(OrderData order) {
        return given()
                .spec(requestSpecification())
                .body(order)
                .when()
                .post(ORDERS_API)
                .then();
    }

    @Step("Получение данных о заказах конкретного пользователя с авторизацией")
    public static ValidatableResponse getOrderDataUserWithAuth(String token) {
        return given()
                .spec(requestSpecificationAuth(token))
                .when()
                .get(ORDERS_API)
                .then();
    }

    @Step("Получение данных о заказах конкретного пользователя без авторизации")
    public static ValidatableResponse getOrderDataUserWithoutAuth() {
        return given()
                .spec(requestSpecification())
                .when()
                .get(ORDERS_API)
                .then();
    }
}
