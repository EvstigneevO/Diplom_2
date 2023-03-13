package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;

import static config.Config.ORDER_PATH;
import static config.Config.baseRqSpec;
import static io.restassured.RestAssured.given;

public class Order {

    @Step("Получение списка заказов")
    public Response getOrders(String token) {
        return given()
                .header("Authorization", token)
                .spec(baseRqSpec)
                .get(ORDER_PATH);
    }

    @Step("Создание заказа")
    public Response createOrder(List<String> ingredients, String token) {
        HashMap<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("ingredients", ingredients);
        return given()
                .header("Authorization", token)
                .spec(baseRqSpec)
                .body(requestBody)
                .when()
                .post(ORDER_PATH);
    }
}
