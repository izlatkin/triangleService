package triangle.service.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;
import io.restassured.mapper.TypeRef;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetAllTriangleTest extends TriangleService{
    private Triangle triangle;

    @BeforeTest
    public void suiteSetUp() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }

    @Test
    public void getAllTest() {
        List<Triangle> triangles = given().
                spec(createDefaultRequestSpec()).
                when().
                get("/triangle/all").
                then().
                log().all().
                statusCode(200).
                extract().as(new TypeRef<List<Triangle>>() {});

        triangle = createTriangle(new TriangleRequest("3;4;5"));

        List<Triangle> trianglesAfterAddition = given().
                spec(createDefaultRequestSpec()).
                when().
                get("/triangle/all").
                then().
                log().all().
                statusCode(200).
                extract().as(new TypeRef<List<Triangle>>() {});

        assertThat(triangles, not(hasItem(hasProperty("id", equalTo(triangle.getId())))));
        assertThat(trianglesAfterAddition, hasItem(hasProperty("id", equalTo(triangle.getId()))));
    }

    @AfterMethod
    public void tearDown() {
        if (triangle != null) {
            TriangleService tc = new TriangleService();
            tc.deleteAllTriangles();
        }
    }
}
