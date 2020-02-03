package triangle.service.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateTriangleTest extends TriangleService {

    private Triangle triangle;

    @DataProvider(name = "createTrianglesSuccessData")
    public Object[][] createTrianglesSuccessData() {
        return new Object[][] {
                { new TriangleRequest("3;4;5"), 3.0, 4.0, 5.0},
                { new TriangleRequest("0.5;0.75;0.5"), 0.5, 0.75, 0.5},
                { new TriangleRequest("3,4,5", ","), 3.0, 4.0, 5.0},
                { new TriangleRequest("3:4:5", ":"), 3.0, 4.0, 5.0},
                { new TriangleRequest("1;1;2"), 1.0, 1.0, 2.0}, // check doc
                { new TriangleRequest("1;1;0"), 1.0, 1.0, 0.0}, // check doc
                { new TriangleRequest("0;0;0"), 0.0, 0.0, 0.0}, // check doc
                { new TriangleRequest("1000000000000000000;1000000000000000000;1000000000000000000"),
                        1000000000000000000.0, 1000000000000000000.0, 1000000000000000000.0}
        };
    }

    @BeforeSuite
    public void suiteSetUp() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }

    @Test(dataProvider = "createTrianglesSuccessData")
    public void createTriangleSuccessTest(TriangleRequest requestPayload,
                                          double firstSide,
                                          double secondSide,
                                          double thirdSide) {
        triangle = given(createDefaultRequestSpec()).
                body(requestPayload).
        when().
                post("/triangle").
        then().
                log().all().
                statusCode(200).
                extract().as(Triangle.class);

        assertThat(triangle.getId(), notNullValue());
        assertThat(triangle.getFirstSide(), equalTo(firstSide));
        assertThat(triangle.getSecondSide(), equalTo(secondSide));
        assertThat(triangle.getThirdSide(), equalTo(thirdSide));

    }

    @DataProvider(name = "createTrianglesUnprocessibleData")
    public Object[][] createTrianglesUnprocessibleData() {
        return new Object[][] {
                { new TriangleRequest("3;4;8")}, // a + b < c
                { new TriangleRequest("3;4;-5")},
                { new TriangleRequest("-3;4;5")},
                { new TriangleRequest("3;-4;5")},
                { new TriangleRequest("1;1;0,5")}, // 0,5 contains ',' instead of '.'
                { new TriangleRequest("3;4;5;6")},
                { new TriangleRequest("3;4;")},
                { new TriangleRequest("3;4;a")},
                { new TriangleRequest("3;4;5", ":")}
        };
    }

    @Test(dataProvider = "createTrianglesUnprocessibleData")
    public void unprocessbleCreateTriangleRequestTest(TriangleRequest requestPayload) {
        given(createDefaultRequestSpec()).
                body(requestPayload).
        when().
                post("/triangle").
        then().
                log().all().
                statusCode(422).
                body("id", is(nullValue()));
    }

    @Test
    public void unauthorizedRequestTest() {
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

    @AfterMethod
    public void tearDown() {
        if (triangle != null) {
            deleteTriangle(triangle.getId());
        }
    }
}
