package triangle.service.tests;

import org.testng.annotations.*;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateTriangleTest extends TriangleService {

    private Triangle triangle;

    @BeforeTest
    public void suiteSetUp() {
        deleteAllTriangles();
    }

    @DataProvider(name = "createTrianglesSuccessData")
    public Object[][] createTrianglesSuccessData() {
        return new Object[][] {
                { new TriangleRequest("3;4;5"), 3.0, 4.0, 5.0},
                { new TriangleRequest("0.5;0.75;0.5"), 0.5, 0.75, 0.5},
                { new TriangleRequest("1;1;2"), 1.0, 1.0, 2.0}, // check doc
                { new TriangleRequest("1;1;0"), 1.0, 1.0, 0.0}, // check doc
                { new TriangleRequest("0;0;0"), 0.0, 0.0, 0.0}, // check doc
                { new TriangleRequest("1000000000000000000;1000000000000000000;1000000000000000000"),
                        1000000000000000000.0, 1000000000000000000.0, 1000000000000000000.0}
        };
    }

    @Test(dataProvider = "createTrianglesSuccessData")
    public void createTriangleSuccessTest(TriangleRequest requestPayload,
                                          double firstSide,
                                          double secondSide,
                                          double thirdSide) {
        triangle = createTriangle(requestPayload);

        assertThat(triangle.getId(), notNullValue());
        assertThat(triangle.getFirstSide(), equalTo(firstSide));
        assertThat(triangle.getSecondSide(), equalTo(secondSide));
        assertThat(triangle.getThirdSide(), equalTo(thirdSide));

    }

    @DataProvider(name = "createTrianglesNumberFormats")
    public Object[][] createTrianglesNumberFormats() {
        return new Object[][] {
                { new TriangleRequest("1;2;3"), 1e0, 2e0, 3e0},
                { new TriangleRequest("1e5;1e5;1e-5"), 1e5, 1e5, 1e-5},
                { new TriangleRequest("1E5;1E5;1e-10"), 1E5, 1E5, 1e-10},
                { new TriangleRequest("2345E10;1234E10;1234.6778E10"), 2345E10,1234E10,1234.6778E10},
                { new TriangleRequest("1e-256;1e-256;1e-256"), 1e-256, 1e-256, 1e-256},
                { new TriangleRequest("9e256;9e256;9e256"), 9e256, 9e256, 9e256}
        };
    }

    @Test(dataProvider = "createTrianglesNumberFormats")
    public void createTriangleSuccessWithhDiffNumberFormats(TriangleRequest requestPayload,
                                          double firstSide,
                                          double secondSide,
                                          double thirdSide) {
        triangle = createTriangle(requestPayload);

        assertThat(triangle.getId(), notNullValue());
        assertThat(triangle.getFirstSide(), equalTo(firstSide));
        assertThat(triangle.getSecondSide(), equalTo(secondSide));
        assertThat(triangle.getThirdSide(), equalTo(thirdSide));

    }

    @DataProvider(name = "createTrianglesUnprocessibleData")
    public Object[][] createTrianglesUnprocessibleData() {
        return new Object[][] {
                { new TriangleRequest("3;4;8")},
                { new TriangleRequest("3;4;-5")},
                { new TriangleRequest("-3;4;5")},
                { new TriangleRequest("3;-4;5")},
                { new TriangleRequest("1;1;0,5")},
                { new TriangleRequest("3;4;5;6")},
                { new TriangleRequest("3;4;")},
                { new TriangleRequest("3;4;a")}
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


    @AfterMethod
    public void tearDown() {
        deleteAllTriangles();
    }
}
