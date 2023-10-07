import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import orders.OrderStep;
import orders.OrderData;
import users.UserData;
import users.UserStep;

import static org.hamcrest.Matchers.equalTo;

public class ReceivingUserOrders {
    UserData user;
    OrderData order;
    private String token;

    @Before
    public void setUp() {
        user = UserData.generateUserRandom();
        order = new OrderData();
    }

    @After
    public void cleanUp() {
        if (token == null) return;
        UserStep.deleteUser(user, token);
    }

    @Test
    @DisplayName("Получить список заказв с авторизацией")
    public void getDataOrderWithLoginTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        OrderStep.createOrderWithAuth(token, order);
        ValidatableResponse responseReceivingOrder = OrderStep.getOrderDataUserWithAuth(token);
        responseReceivingOrder
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получить список заказв без авторизации")
    public void getDataOrderWithoutLoginTest() {
        ValidatableResponse responseReceivingOrder = OrderStep.getOrderDataUserWithoutAuth();
        responseReceivingOrder
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("You should be authorised"));
    }
}
