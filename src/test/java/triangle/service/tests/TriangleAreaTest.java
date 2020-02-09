package triangle.service.tests;

import org.testng.annotations.*;
import triangle.service.Result;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TriangleAreaTest extends TriangleService{
    private Triangle triangle;

    @DataProvider(name = "areas")
    public Object[][] createTriangleAreasData() {
        return new Object[][] {
                // Expected area is calculated according to Heron's formula
                { "3;4;5", triangleAre(3,4,5) },
                { "0.5;0.75;1.0", triangleAre(0.5,0.75,1.0)},
                { "1000000.001;1000000;1000000", triangleAre(1000000.001,1000000,1000000)},
                { "1;1;0", triangleAre(1,1,0)},
                { "1;1;2", triangleAre(1,1,2)},
                { "0;0;0", triangleAre(0,0,0)}
        };
    }

    @BeforeTest
    public void suiteSetUp() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }

    @Test(dataProvider = "areas")
    public void getTriangleAreaTest(String input, double expectedResult) {
        triangle = createTriangle(new TriangleRequest(input));

        Result result = given().
                spec(createDefaultRequestSpec()).
                when().
                pathParam("triangleId", triangle.getId()).
                get("/triangle/{triangleId}/area").
                then().
                log().all().
                statusCode(200).
                extract().as(Result.class);

        assertThat(Double.valueOf(result.getResult()), closeTo(expectedResult,expectedResult));
    }

    @Test
    public void notFoundTest() {
        given().
                spec(createDefaultRequestSpec()).
                when().
                pathParam("triangleId", UUID.randomUUID().toString()).
                get("/triangle/{triangleId}/area").
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
