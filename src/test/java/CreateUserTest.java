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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Epic("Stellar Burgers")
@Feature("Create User")
@DisplayName("Создание пользователя")
public class CreateUserTest {
    private User user;
    String accessToken;

    private Map<String, String> credentials() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String username = RandomStringUtils.randomAlphabetic(10);
        Map<String, String> inputDataMap = new HashMap<>();
        inputDataMap.put("username", username);
        inputDataMap.put("email", email);
        inputDataMap.put("password", password);
        return inputDataMap;
    }

    @Before
    public void setUp() {
        user = new User();
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            user.deleteUser(accessToken);
        }
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Создание пользователя с валидными данными")
    public void successfullyCreateUserTest() {
        Map<String, String> data = credentials();
        Response response = user.createUser(data.get("email"), data.get("password"), data.get("username"));
        accessToken = response.path("accessToken");
        assertEquals("Неверный код ответа", 200, response.statusCode());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Повторное создание пользователя")
    public void createUserThatAlreadyRegisteredTest() {
        Map<String, String> data = credentials();
        Response firstUser = user.createUser(data.get("email"), data.get("password"), data.get("username"));
        accessToken = firstUser.path("accessToken");
        Response response = user.createUser(data.get("email"), data.get("password"), data.get("username"));
        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertEquals("Невалидные данные в ответе: message", "User already exists", response.path("message"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание пользователя без заполнения одного из обязательных полей")
    public void createUserWithoutFillingInOneRequiredFieldsTest() {
        Map<String, String> data = credentials();
        Response response = user.createUser(data.get("email"), data.get(""), data.get("username"));
        accessToken = response.path("accessToken");
        assertEquals("Неверный код ответа", 403, response.statusCode());
        assertEquals("Невалидные данные в ответе: message", "Email, password and name are required fields", response.path("message"));
    }
}

