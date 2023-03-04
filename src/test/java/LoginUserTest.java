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
@Feature("Login user")
@DisplayName("Авторизация пользователя")
public class LoginUserTest {
    User user;
    String accessToken;

    private Map<String, String> createData() {
        String username = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        Response response = user.createUser(email, password, username);
        String token = response.path("accessToken");
        Map<String, String> inputDataMap = new HashMap<>();
        inputDataMap.put("email", email);
        inputDataMap.put("password", password);
        inputDataMap.put("name", username);
        inputDataMap.put("accessToken", token);
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
    @DisplayName("Логин существующим пользователем")
    public void successfullyLoginExistingUserTest() {
        Map<String, String> data = createData();
        Response response = user.loginUser(data.get("email"), data.get("password"));
        accessToken = response.path("accessToken");
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Логин с неверным логином и паролем")
    public void loginWithIncorrectUsernameAndPasswordTest() {
        Map<String, String> data = createData();
        accessToken = data.get("accessToken");
        Response response = user.loginUser(data.get("email"), "incorrect");
        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "email or password are incorrect", response.path("message"));
    }
}
