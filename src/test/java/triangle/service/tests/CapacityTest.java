package triangle.service.tests;

import io.restassured.mapper.TypeRef;
import org.testng.annotations.*;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import java.util.List;
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
        deleteAllTriangles();
    }

    @Test
    public void maxCapacityAndAbove() {
        //post triangles till the end of capacity /maxCapacity/
        addTriangles(maxCapacity);
        assertThat(getAllTriangles().size(), equalTo(maxCapacity));
        // post 10 extra triangle
        int aboveCapacity = maxCapacity + 1;
        //check that it is not possible to add more trianles
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

    @Test
    public void capacityAfterFailAttempt() {
        //post triangles till the end of capacity /maxCapacity/
        addTriangles(maxCapacity - 1);

        given(createDefaultRequestSpec()).
                body(new TriangleRequest(1 + ";" + 2 + ";" + "corrupted part")).
                when().
                post("/triangle").
                then().
                log().all().
                statusCode(422).
                body("id", is(nullValue()));

        //check that capacity did not hanges after fail ttemp to add triangle
        assertThat(getAllTriangles().size(), equalTo(maxCapacity - 1));

    }

    @Test
    public void zeroAndOneCapacity() {
        assertThat(getAllTriangles().size(), equalTo(0));
        triangle = createTriangle(new TriangleRequest(2 + ";" + 3 + ";" + 4));
        assertThat(getAllTriangles().size(), equalTo(1));

    }

    @AfterMethod
    public void tearDown() {
        deleteAllTriangles();
    }
}
