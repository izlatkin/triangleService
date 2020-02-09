package triangle.service.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;
import io.restassured.mapper.TypeRef;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetAllTriangleTest extends TriangleService{
    private Triangle triangle;

    @BeforeTest
    public void suiteSetUp() {
        deleteAllTriangles();
    }

    @Test
    public void getAllTest() {
        addTriangles(new Random().nextInt(9));
        List<Triangle> triangles = getAllTriangles();
        triangle = createTriangle(new TriangleRequest("3;4;5"));
        List<Triangle> trianglesAfterAddition = getAllTriangles();
        assertThat(triangles, not(hasItem(hasProperty("id", equalTo(triangle.getId())))));
        assertThat(trianglesAfterAddition, hasItem(hasProperty("id", equalTo(triangle.getId()))));
    }

    @AfterMethod
    public void tearDown() {
        deleteAllTriangles();
    }
}
