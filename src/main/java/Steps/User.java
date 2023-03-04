package Steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;

import static config.Config.USER_PATH;
import static config.Config.baseRqSpec;
import static io.restassured.RestAssured.given;

public class User {
    @Step("Создание пользователя")
    public Response createUser(String email, String password, String username) {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("name", username);
        return given()
                .spec(baseRqSpec)
                .body(requestBody)
                .when()
                .post(USER_PATH + "register/");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(String email, String password) {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        return given()
                .spec(baseRqSpec)
                .body(requestBody)
                .when()
                .post(USER_PATH + "login/");
    }

    @Step("Изменение данных пользователя")
    public Response editUser(String email, String password, String username, String token) {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("name", username);
        return given()
                .header("Authorization", token)
                .spec(baseRqSpec)
                .body(requestBody)
                .when()
                .patch(USER_PATH + "user/");
    }

    @Step("Удаление пользователя.")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .spec(baseRqSpec)
                .delete(USER_PATH + "user/")
                .then()
                .statusCode(202);
    }
}
