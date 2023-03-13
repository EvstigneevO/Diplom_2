import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.User;

import java.util.Map;

import static org.junit.Assert.*;
import static utils.UserGenerator.generateUserData;

@Epic("Stellar Burgers")
@Feature("Login")
@DisplayName("Авторизация пользователя")
public class LoginUserTest {
    User user;
    String accessToken;

    private Map<String, String> createData() {
        Map<String, String> data = generateUserData();
        Response response = user.createUser(data.get("email"), data.get("password"), data.get("username"));
        data.put("accessToken", response.path("accessToken"));
        return data;
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
    @DisplayName("Авторизация существующим пользователем")
    public void successfullyLoginExistingUserTest() {
        Map<String, String> data = createData();
        Response response = user.loginUser(data.get("email"), data.get("password"));
        accessToken = response.path("accessToken");
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertTrue("Невалидные данные в ответе: success", response.path("success"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Авторизация с неверным логином и паролем")
    public void loginWithIncorrectUsernameAndPasswordTest() {
        Map<String, String> data = createData();
        accessToken = data.get("accessToken");
        Response response = user.loginUser(data.get("email"), "incorrect");
        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertFalse("Невалидные данные в ответе: success", response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "email or password are incorrect", response.path("message"));
    }
}
