package utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

public class UserGenerator {
    public static Map<String,String> generateUserData(){
        Map<String,String> userData = new HashMap<>();
        userData.put("username", RandomStringUtils.randomAlphabetic(10));
        userData.put("email",RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userData.put("password",RandomStringUtils.randomAlphabetic(10));
        return userData;
    }
}
