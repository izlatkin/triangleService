package triangle.service.tests;

import org.testng.annotations.Test;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static triangle.service.tests.TriangleService.createUnauthorizedRequestSpec;

public class AuthorizationTest extends TriangleService{
    private Triangle triangle;

    @Test
    public void unauthorizedRequestPostTriangleTest() {
        given().
                spec(createUnauthorizedRequestSpec()).
                body(new TriangleRequest("3;4;5")).
                when().
                post("/triangle").
                then().
                log().all().
                statusCode(401).
                body("error", equalTo("Unauthorized"));
    }

    @Test
    public void unauthorizedRequestDeleteTriangleTest() {
        triangle = createTriangle(new TriangleRequest(2 + ";" + 3 + ";" + 4));

        given().
                spec(createUnauthorizedRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                delete("/triangle/{triangleId}").
                then().
                log().all().
                statusCode(401).
                body("error", equalTo("Unauthorized"));
    }

    @Test
    public void unauthorizedRequestGetTriangleAllTest() {
        given().
                spec(createUnauthorizedRequestSpec()).
                when().
                get("/triangle/all").
                then().
                log().all().
                statusCode(401).
                body("error", equalTo("Unauthorized"));
    }

    @Test
    public void unauthorizedRequestTriangleAreaTest() {
        triangle = createTriangle(new TriangleRequest("3;4;5"));

        given().
                spec(createUnauthorizedRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                get("/triangle/{triangleId}/area").
                then().
                log().all().
                statusCode(401).
                body("error", equalTo("Unauthorized"));
    }

    @Test
    public void unauthorizedRequestTrianglePerimeterTest() {
        triangle = createTriangle(new TriangleRequest("3;4;5"));

        given().
                spec(createUnauthorizedRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                get("/triangle/{triangleId}/perimeter").
                then().
                log().all().
                statusCode(401).
                body("error", equalTo("Unauthorized"));
    }
}
