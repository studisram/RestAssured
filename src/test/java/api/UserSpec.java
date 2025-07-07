package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserSpec {
    public static RequestSpecification userReqSpec(String url) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setContentType(ContentType.JSON)
                .addHeader("x-api-key", "reqres-free-v1")
                .build();
    }

    public static ResponseSpecification userSuccessRespSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    public static ResponseSpecification userUnsuccessfulRespSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification userResponseSpecWithStatusCode(int code) {
        return new ResponseSpecBuilder()
                .expectStatusCode(code)
                .build();
    }
}
