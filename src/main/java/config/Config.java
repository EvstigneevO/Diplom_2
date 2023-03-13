package config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Config {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";
    public static final String INGREDIENTS_PATH = "api/ingredients";
    public static final String ORDER_PATH = "api/orders";
    public static final String USER_PATH = "api/auth/";
    public static RequestSpecification baseRqSpec = given()
            .log().all()
            .contentType(ContentType.JSON)
            .baseUri(BASE_URL)
            .filters(new AllureRestAssured(), new ResponseLoggingFilter());
}

