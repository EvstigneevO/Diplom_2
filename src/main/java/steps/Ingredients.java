package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static config.Config.INGREDIENTS_PATH;
import static config.Config.baseRqSpec;
import static io.restassured.RestAssured.given;

public class Ingredients {
    @Step("Получение данных об ингредиентах")
    public Response getIngredients() {
        return given()
                .spec(baseRqSpec)
                .get(INGREDIENTS_PATH);
    }
}
