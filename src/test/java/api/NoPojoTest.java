package api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class NoPojoTest {
    private final static String DOMAIN = "https://reqres.in";

    @BeforeClass
    public void beforeClass() {
        RestAssured.requestSpecification = UserSpec.userReqSpec(DOMAIN);
    }

    @Test(description = "Проверка Аватара и ID")
    public void checkAvatarAndIdTest() {
        Response response = given()
                .when()
                .get("/api/users?page=2")
                .then().log().all()
                .spec(UserSpec.userSuccessRespSpec())
                .body("page", equalTo(2)) // проверяет что у ключа "page" значение 2
                .body("data.id", notNullValue()) // проверяет что у "data" -> "id" не пустое
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> emails = jsonPath.getList("data.email");
        List<Integer> ids = jsonPath.getList("data.id");
        List<String> avatars = jsonPath.getList("data.avatar");

        emails.forEach(email -> Assert.assertTrue(email.endsWith("@reqres.in")));

        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }
    }

    @Test(description = "Проверка регистрации")
    public void userRegTest() {
        // Ожидаемые данные
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", "eve.holt@reqres.in");
        userData.put("password", "pistol");

        Response response = given()
                .body(userData)
                .log().headers()
                .when()
                .post("/api/register")
                .then()
                .spec(UserSpec.userSuccessRespSpec())
                .log().all()
                .extract().response();

        Assert.assertEquals(id.toString(), response.body().jsonPath().getString("id"));
        Assert.assertEquals(token, response.body().jsonPath().getString("token"));
    }
}
