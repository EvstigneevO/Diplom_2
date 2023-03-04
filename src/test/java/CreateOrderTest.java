import Steps.Ingredients;
import Steps.Order;
import Steps.User;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Epic("Stellar Burgers")
@Feature("Create order")
@DisplayName("Создание заказа")
public class CreateOrderTest {
    User user;
    Order order;
    Ingredients ingredients;
    String accessToken;


    private String getAccessTokenFromUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        return user.createUser(email, password, username).path("accessToken");
    }

    @Before
    public void setUp() {
        user = new User();
        order = new Order();
        ingredients = new Ingredients();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            user.deleteUser(accessToken);
        }
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    public void successfullyCreateOrderWithAuthorizationTest() {
        accessToken = getAccessTokenFromUser();
        Response responseIngredients = ingredients.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        Response response = order.createOrder(ingredients, accessToken);
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));

    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание заказа без авторизации")
    public void successfullyCreateOrderWithoutAuthorizationTest() {
        Response responseIngredients = ingredients.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        Response response = order.createOrder(ingredients, "");
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        accessToken = getAccessTokenFromUser();
        Response response = order.createOrder(null, accessToken);
        assertEquals("Неверный код ответа", 400, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "Ingredient ids must be provided", response.path("message"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHashTest() {
        accessToken = getAccessTokenFromUser();
        Response response = order.createOrder(List.of("InvalidIngredientHash"), accessToken);
        assertEquals("Неверный код ответа", 500, response.statusCode());
    }
}
