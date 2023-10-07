import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import orders.OrderStep;
import orders.OrderData;
import users.UserData;
import users.UserStep;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
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
    @DisplayName("Создание заказа с ингридиентами с авторизацией")
    public void createOrderWithIngredientsTest() {
        order = new OrderData(getIngredientList());
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = OrderStep.createOrderWithAuth(token, order);
        responseCreateOrder
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами без авторизации")
    public void createOrderWithoutLoginTest() {
        order = new OrderData(getIngredientList());
        ValidatableResponse responseCreateOrder = OrderStep.createOrderWithoutAuth(order);
        responseCreateOrder
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов с авторизацией")
    public void createOrderWithoutIngredientsTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = OrderStep.createOrderWithAuth(token, order);
        responseCreateOrder
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неправильным хешем ингредиентов")
    public void createOrderWithBrokenIngredientsTest() {
        order = new OrderData(getBrokenIngredientList());
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = OrderStep.createOrderWithAuth(token, order);
        responseCreateOrder
                .statusCode(500);
    }

    private List<String> getIngredientList() {
        ValidatableResponse validatableResponse = OrderStep.getIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(2));
        ingredients.add(list.get(4));
        ingredients.add(list.get(0));
        return ingredients;
    }

    private List<String> getBrokenIngredientList() {
        ValidatableResponse validatableResponse = OrderStep.getIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(2).repeat(2));
        ingredients.add(list.get(4).repeat(1));
        ingredients.add(list.get(0));
        return ingredients;
    }
}
