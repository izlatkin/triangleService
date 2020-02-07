package triangle.service.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteTest  extends TriangleService{
    private Triangle triangle;

    @BeforeTest
    public void suiteSetUp() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }

    @Test
    public void deleteTriangleTest() {
        triangle = createTriangle(new TriangleRequest(2 + ";" + 3 + ";" + 4));

        given().
                spec(createDefaultRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                delete("/triangle/{triangleId}").
                then().
                log().all().
                statusCode(200);

        // check that triangle not exist
        given().
                spec(createDefaultRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                get("/triangle/{triangleId}").
                then().
                log().all().
                statusCode(404);
    }

    @Test
    public void notFoundTest() {
        given().
                spec(createDefaultRequestSpec()).
                when().
                pathParam("triangleId",
                        UUID.randomUUID().toString()).
                delete("/triangle/{triangleId}").
                then().
                log().all().
                statusCode(200);

    }

    @AfterMethod
    public void tearDown() {
        if (triangle != null) {
            TriangleService tc = new TriangleService();
            tc.deleteAllTriangles();
        }
    }
}
