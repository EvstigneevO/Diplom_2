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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@Epic("Stellar Burgers")
@Feature("Get orders")
@DisplayName("Получение заказов")
public class GetOrderTest {
    User user;
    Order order;
    Ingredients ingredients;
    String accessToken;

    private String createAndLoginUser() {
        String username = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        Response response = user.createUser(email, password, username);
        user.loginUser(email, password);
        return response.path("accessToken");
    }

    @Before
    public void setUp() {
        user = new User();
        order = new Order();
        ingredients = new Ingredients();
        accessToken = createAndLoginUser();
        Response responseIngredients = ingredients.getIngredients();
        List<String> ingredients = responseIngredients.path("data._id");
        order.createOrder(ingredients, accessToken);
    }

    @After
    public void cleanUp() {
        user.deleteUser(accessToken);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void successfullyReceiveOrderListTest() {
        Response response = order.getOrders(accessToken);
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
        assertThat("Заказа не существует", response.path("orders"), notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказ без авторизации")
    public void receiveOrderListWithoutAuthorizationTest() {
        Response response = order.getOrders("");
        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }
}
