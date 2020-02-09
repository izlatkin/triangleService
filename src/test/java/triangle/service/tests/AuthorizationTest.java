package triangle.service.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthorizationTest extends TriangleService{
    private Triangle triangle;

    @BeforeTest
    public void suiteSetUp() {
        deleteAllTriangles();
    }

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
    public void unauthorizedRequestGetTriangleTest() {
        triangle = createTriangle(new TriangleRequest(2 + ";" + 3 + ";" + 4));

        given().
                spec(createUnauthorizedRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                get("/triangle/{triangleId}").
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

    @DataProvider(name = "notValidHeaders")
    public Object[][] notValidHeaders() {
        return new Object[][] {
                {"_X-User", "38e1e2f8-5428-4833-a38b-054fb6522a95"},
                {"X-User", "38e1e2f8-5428-4833-a38b-054fb6522a95_"},
                { "X-User", ""},
                {"", "38e1e2f8-5428-4833-a38b-054fb6522a95"},
                {"X-User_", "38e1e2f8-5428-4833-a38b-054fb6522a95_"},
                {" X-User", "38e1e2f8-5428-4833-a38b-054fb6522a95"},
                {"X-User ", "38e1e2f8-5428-4833-a38b-054fb6522a95"},
                {"X-User ", "38e1e2f8-5428-4833-a38b-054fb6522a95 "},
                {"X-User", " 38e1e2f8-5428-4833-a38b-054fb6522a95"},
        };
    }

    @Test(dataProvider = "notValidHeaders")
    protected void headersTest(String key, String value) {
        RequestSpecBuilder rb = createCommonRequestSpecBuilder();
        RequestSpecification rc = rb.addHeader(key, value).
                build();
        given(rc).body(new TriangleRequest("3;4;5")).
                when().
                post("/triangle").
                then().
                log().all().
                statusCode(401).
                body("error", equalTo("Unauthorized"));
    }

    @AfterMethod
    public void tearDown() {
        deleteAllTriangles();
    }
}
