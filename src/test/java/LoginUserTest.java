import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import users.UserData;
import users.UserStep;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    UserData user;
    private String token;


    @Before
    public void setup() {
        user = UserData.generateUserRandom();
    }
    @After
    public void cleanUp() {
        if (token == null) return;
        UserStep.deleteUser(user, token);
    }
    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginWithCorrectUserDataTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        ValidatableResponse responseLogin = UserStep.loginUser(user);
        responseLogin
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", is(notNullValue()));
    }

    @Test
    @DisplayName("Логин с неверным логином")
    public void loginWithInvalidLoginTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        user.setEmail(user.getEmail() + "invalid");
        ValidatableResponse responseLogin = UserStep.loginUser(user);
        responseLogin
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithInvaliPasswordTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        user.setPassword(user.getPassword() + "invalid");
        ValidatableResponse responseLogin = UserStep.loginUser(user);
        responseLogin
                .statusCode(401)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
