package triangle.service.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TriangleService{

    private static RequestSpecBuilder createCommonRequestSpecBuilder() {
        return new RequestSpecBuilder().
                setBaseUri("https://qa-quiz.natera.com/").
                setContentType("application/json").
                log(LogDetail.ALL);
    }

    protected static RequestSpecification createDefaultRequestSpec() {
        return createCommonRequestSpecBuilder().
                addHeader("X-User", "38e1e2f8-5428-4833-a38b-054fb6522a95").
                build();
    }

    protected static RequestSpecification createUnauthorizedRequestSpec() {
        return createCommonRequestSpecBuilder().
                build();
    }

    public void deleteAllTriangles() {
        List<String> trianglesIds = given(createDefaultRequestSpec()).
                get("/triangle/all").
                then().
                extract().path("id");
        for (String id : trianglesIds) {
            deleteTriangle(id);
        }
    }

    protected void deleteTriangle(String triangleId) {
        given(createDefaultRequestSpec()).
                pathParam("triangleId", triangleId).
                delete("triangle/{triangleId}");
    }

    protected Triangle createTriangle(TriangleRequest requestPayload) {
        return given(createDefaultRequestSpec()).
                body(requestPayload).
                when().
                post("/triangle").
                then().
                statusCode(200).
                body("id", not(empty())).
                extract().
                as(Triangle.class);
    }
}

