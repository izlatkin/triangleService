package triangle.service.tests;

import org.testng.annotations.*;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.nullValue;
import static triangle.service.tests.TriangleService.createDefaultRequestSpec;

public class SeparatorTest {
    private Triangle triangle;

    @BeforeTest
    public void suiteSetUp() {
        TriangleService tc = new TriangleService();
        tc.deleteAllTriangles();
    }

    @DataProvider(name = "SeparatorData")
    public Object[][] SeparatorData() {
        return new Object[][] {
                { new TriangleRequest("3;4;5", ";"), 3.0, 4.0, 5.0},
                { new TriangleRequest("3.4.5", "."), 3.0, 4.0, 5.0},
                { new TriangleRequest("3,4,5", ","), 3.0, 4.0, 5.0},
                { new TriangleRequest("3:4:5", ":"), 3.0, 4.0, 5.0},
                { new TriangleRequest("3 4 5", " "), 3.0, 4.0, 5.0},
                { new TriangleRequest("3  4  5", "  "), 3.0, 4.0, 5.0},
                { new TriangleRequest("3\t4\t5", "\t"), 3.0, 4.0, 5.0},
                { new TriangleRequest("3\n4\n5", "\n"), 3.0, 4.0, 5.0},
                { new TriangleRequest("3a4a5", "a"), 3.0, 4.0, 5.0},
                { new TriangleRequest("3ж4ж5", "ж"), 3.0, 4.0, 5.0},
                { new TriangleRequest("3long_separator4long_separator5", "long_separator"), 3.0, 4.0, 5.0},
                { new TriangleRequest("1000000000000000000:1000000000000000000:1000000000000000000",":"),
                        1000000000000000000.0, 1000000000000000000.0, 1000000000000000000.0}
        };
    }

    @Test(dataProvider = "SeparatorData")
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

    @DataProvider(name = "separatorUnprocessibleData")
    public Object[][] createTrianglesUnprocessibleData() {
        return new Object[][] {
                { new TriangleRequest("3:4:8",";")},
//                { new TriangleRequest("3;4;-5")},
//                { new TriangleRequest("-3;4;5")},
//                { new TriangleRequest("3;-4;5")},
//                { new TriangleRequest("1;1;0,5")},
//                { new TriangleRequest("3;4;5;6")},
//                { new TriangleRequest("3;4;")},
//                { new TriangleRequest("3;4;a")},
                { new TriangleRequest("3;4;5", ":")}
        };
    }

    @Test(dataProvider = "separatorUnprocessibleData")
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
        if (triangle != null) {
            TriangleService tc = new TriangleService();
            tc.deleteAllTriangles();
        }
    }
}
