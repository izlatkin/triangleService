package triangle.service.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;

public class CapacityTest extends TriangleService {
    private Triangle triangle;
    private Random rd = new Random();
    public  static final int maxCapacity = 10;

    @BeforeTest
    public void suiteSetUp() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }

    @Test
    public void maxCapacityAndAbove() {
        //post triangles till the end of capacity /maxCapacity/
        for (int i = 0;i <= maxCapacity; i++) {

            triangle = given(createDefaultRequestSpec()).
                    body(new TriangleRequest(i + ";"+ i + ";"+ i)).
                    when().
                    post("/triangle").
                    then().
                    log().all().
                    statusCode(200).
                    extract().as(Triangle.class);

            assertThat(triangle.getId(), notNullValue());
            assertThat(triangle.getFirstSide(), equalTo((double)i));
            assertThat(triangle.getSecondSide(), equalTo((double)i));
            assertThat(triangle.getThirdSide(), equalTo((double)i));
        }
        // post 10 extra triangle
        int aboveCapacity = maxCapacity + 1;
        for (int i = aboveCapacity; i < 2 * maxCapacity; i++) {
            given(createDefaultRequestSpec()).
                    body(new TriangleRequest(i + ";" + i + ";" + i)).
                    when().
                    post("/triangle").
                    then().
                    log().all().
                    statusCode(422).
                    body("id", is(nullValue()));
        }
    }

    @AfterMethod
    public void tearDown() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }
}
