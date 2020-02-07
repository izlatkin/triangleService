package triangle.service.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import triangle.service.Result;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

public class TrianglePerimeterTest extends TriangleService {
    private Triangle triangle;

    @BeforeTest
    public void suiteSetUp() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }

    @DataProvider(name = "perimeters")
    public Object[][] createTrianglePerimetersData() {
        return new Object[][] {
                { "3;4;5", 12.0 },
                { "0.5;0.75;0.5", 1.75},
                { "1000000.001;1000000.002;1000000.003", 3_000_000.006},
                { "1;1;0", 2},
                { "1;1;2", 4},
                { "0;0;0", 0}
        };
    }

    @Test(dataProvider = "perimeters")
    public void getTrianglePerimeterTest(String input, double expectedPerimeter) {
        triangle = createTriangle(new TriangleRequest(input));

        Result result = given().
                spec(createDefaultRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                get("/triangle/{triangleId}/perimeter").
                then().
                log().all().
                statusCode(200).
                extract().as(Result.class);

        assertThat(Double.valueOf(result.getResult()), closeTo(expectedPerimeter, 1E-6));
    }

    @Test
    public void notFoundTest() {
        given().
                spec(createDefaultRequestSpec()).
                when().
                pathParam("triangleId", UUID.randomUUID().toString()).
                get("/triangle/{triangleId}/perimeter").
                then().
                log().all().
                statusCode(404).
                body("error", equalTo("Not Found"));
    }

    @AfterMethod
    public void tearDown() {
        if (triangle != null) {
            TriangleService tc = new TriangleService();
            tc.deleteAllTriangles();
        }
    }

}
