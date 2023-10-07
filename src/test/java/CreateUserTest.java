import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import users.UserData;
import users.UserStep;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    UserData user;
    private String token;

    @Before
    public void setUp() {
        user = UserData.generateUserRandom();
    }

    @After
    public void cleanUp() {
        if (token == null) return;
        UserStep.deleteUser(user, token);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest() {
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        responseReg
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createAlreadyRegisteredUserTest(){
        ValidatableResponse responseReg = UserStep.createUser(user);
        token = responseReg.extract().path("accessToken");
        ValidatableResponse responseRegDuplicate = UserStep.createUser(user);
        responseRegDuplicate
                .statusCode(403)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailTest(){
        user.setEmail(null);
        ValidatableResponse responseReg = UserStep.createUser(user);
        responseReg
                .statusCode(403)
                .assertThat()
                .body("success",equalTo(false))
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest(){
        user.setPassword(null);
        ValidatableResponse responseReg = UserStep.createUser(user);
        responseReg
                .statusCode(403)
                .assertThat()
                .body("success",equalTo(false))
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest(){
        user.setName(null);
        ValidatableResponse responseReg = UserStep.createUser(user);
        responseReg
                .statusCode(403)
                .assertThat()
                .body("success",equalTo(false))
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }
}
