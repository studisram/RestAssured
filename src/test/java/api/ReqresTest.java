package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

import static io.restassured.RestAssured.*;

public class ReqresTest {
    private final static String DOMAIN = "https://reqres.in";

    @BeforeClass
    public void beforeClass() {
        RestAssured.requestSpecification = UserSpec.userReqSpec(DOMAIN);
    }

    @Test(description = "Проверка Аватара и ID")
    public void checkAvatarAndIdTest() {
        List<UserData> users = given()
                .when()
                .get("/api/users?page=2")
                .then().log().all()
                .spec(UserSpec.userSuccessRespSpec())
                // Конвертируем в POJO класс
                .extract().body().jsonPath().getList("data", UserData.class);

        // Проверка через forEach
        users.forEach(user -> Assert.assertTrue(user.getAvatar().contains(user.getId().toString())));
        users.forEach(user -> Assert.assertTrue(user.getEmail().endsWith("reqres.in")));

        // Сначала создаем списки - потом проверяем через цикл
        List<String> avatars = users.stream().map(UserData::getAvatar).toList();
        List<String> ids = users.stream().map(user -> user.getId().toString()).toList();

        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test(description = "Проверка регистрации")
    public void userRegTest() {
        // Ожидаемые данные
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";

        RegisterData user = new RegisterData("eve.holt@reqres.in", "pistol");

        SuccessReg registration = given()
                .body(user)
                .log().headers()
                .when()
                .post("/api/register")
                .then()
                .spec(UserSpec.userSuccessRespSpec())
                .log().all()
                .extract().as(SuccessReg.class);
        Assert.assertEquals(id, registration.getId());
        Assert.assertEquals(token, registration.getToken());
    }

    @Test(description = "Проверка НЕУСПЕШНОЙ регистрации")
    public void userUnsuccessfulRegTest() {
        UnsuccessRegData user = new UnsuccessRegData("sydney@fife");

        // Записываем результат в объект Response
        Response response = given().body(user)
                .log().headers()
                .when()
                .post("/api/register")
                .then()
                .log().all()
                .spec(UserSpec.userUnsuccessfulRespSpec())
                .extract().response();

        // Достаем текст ошибки по ключу "error"
        String errorMessage = response.body().jsonPath().getString("error");
        Assert.assertEquals(errorMessage, "Missing password");
    }

    @Test(description = "Проверка сортировки ответа по дате")
    public void checkSortByYearTest() {
        List<ColorsData> colors = given()
                .log().headers()
                .when()
                .get("/api/unknown")
                .then()
                .log().all()
                .spec(UserSpec.userSuccessRespSpec())
                .extract().body().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = colors.stream().map(ColorsData::getYear).toList();
        List<Integer> sortedYears = years.stream().sorted().toList();
        Assert.assertEquals(sortedYears, years);
    }

    @Test(description = "Проверка удаления пользователя")
    public void deleteUserTest() {
        given().log().headers()
                .when()
                .delete("/api/users/2")
                .then().log().all()
                .spec(UserSpec.userResponseSpecWithStatusCode(204));
    }
}
