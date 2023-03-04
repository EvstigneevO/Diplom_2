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
@Feature("Edit User")
@DisplayName("Изменение пользователя")
public class EditUserTest {
    private User user;
    String accessToken;

    private void login(String email, String password) {
        user.loginUser(email, password);
    }

    private Map<String, String> createData() {
        String username = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
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
    @DisplayName("Изменение имени с авторизацией")
    public void successfullyChangeUsernameVerificationWithAuthorizationTest() {
        Map<String, String> data = createData();
        login(data.get("email"), data.get("password"));
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), data.get("password"), newUsername, data.get("accessToken"));
        accessToken = data.get("accessToken");
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение пароля с авторизацией")
    public void successfullyChangePasswordWithAuthorizationTest() {
        Map<String, String> data = createData();
        login(data.get("email"), data.get("password"));
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), newPassword, data.get("name"), data.get("accessToken"));
        accessToken = data.get("accessToken");
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение электронной почты с авторизацией")
    public void successfullyChangeEmailWithAuthorizationTest() {
        Map<String, String> data = createData();
        login(data.get("email"), data.get("password"));
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        Response response = user.editUser(newEmail, data.get("password"), data.get("name"), data.get("accessToken"));
        accessToken = data.get("accessToken");
        assertEquals("Неверный код ответа", 200, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", true, response.path("success"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение имени без авторизации")
    public void changeUsernameWithoutAuthorizationTest() {
        Map<String, String> data = createData();
        login(data.get("email"), data.get("password"));
        String newUsername = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), data.get("password"), newUsername, "");
        accessToken = data.get("accessToken");
        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение пароля без авторизации")
    public void changePasswordWithoutAuthorizationTest() {
        Map<String, String> data = createData();
        login(data.get("email"), data.get("password"));
        String newPassword = RandomStringUtils.randomAlphabetic(10);
        Response response = user.editUser(data.get("email"), newPassword, data.get("name"), "");
        accessToken = data.get("accessToken");
        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Изменение электронной почты без авторизации")
    public void changeEmailWithoutAuthorizationTest() {
        Map<String, String> data = createData();
        login(data.get("email"), data.get("password"));
        String newEmail = RandomStringUtils.randomAlphabetic(5) + "@gmail.com";
        Response response = user.editUser(newEmail, data.get("password"), data.get("name"), "");
        accessToken = data.get("accessToken");
        assertEquals("Неверный код ответа", 401, response.statusCode());
        assertEquals("Невалидные данные в ответе: success", false, response.path("success"));
        assertEquals("Невалидные данные в ответе: message", "You should be authorised", response.path("message"));
    }
}
