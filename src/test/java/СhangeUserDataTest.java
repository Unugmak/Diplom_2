import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import users.UserData;
import users.UserNewData;
import users.UserStep;

import static org.hamcrest.Matchers.*;

public class СhangeUserDataTest {
    UserData user;
    UserNewData userNewData;
    private String token;

    @Before
    public void setUp() {
        user = UserData.generateUserRandom();
        userNewData = UserNewData.generateNewUserDataRandom();
    }

    @After
    public void cleanUp() {
        if (token == null) return;
        UserStep.deleteUser(user, token);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией.")
    public void changeUserDataWithAuthTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        ValidatableResponse responseUpdate = UserStep.changeDataWithAuth(userNewData, token);
        responseUpdate
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void changeUserDataWithoutAuthTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        ValidatableResponse responseUpdate = UserStep.changeDataWithoutAuth(userNewData);
        responseUpdate
                .statusCode(401)
                .assertThat()
                .body("success",equalTo(false))
                .and()
                .body("message",equalTo("You should be authorised"));
    }

}
