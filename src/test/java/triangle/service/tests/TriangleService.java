package triangle.service.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.mapper.TypeRef;
import io.restassured.specification.RequestSpecification;
import triangle.service.Triangle;
import triangle.service.TriangleRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TriangleService{
    private Triangle triangle;

    protected static RequestSpecBuilder createCommonRequestSpecBuilder() {
        return new RequestSpecBuilder().
                setBaseUri("https://qa-quiz.natera.com/").
                setContentType("application/json").
                log(LogDetail.ALL);
    }

    protected static RequestSpecification createDefaultRequestSpec() {
        return createCommonRequestSpecBuilder().
                addHeader("X-User", "38e1e2f8-5428-4833-a38b-054fb6522a95").
                build();
    }

    protected static RequestSpecification createUnauthorizedRequestSpec() {
        return createCommonRequestSpecBuilder().
                build();
    }

    public void deleteAllTriangles() {
        List<String> trianglesIds = given(createDefaultRequestSpec()).
                get("/triangle/all").
                then().
                extract().path("id");
        for (String id : trianglesIds) {
            deleteTriangle(id);
        }
    }

    public List<Triangle> getAllTriangles() {
        return given().
                spec(createDefaultRequestSpec()).
                when().
                get("/triangle/all").
                then().
                log().all().
                statusCode(200).
                extract().as(new TypeRef<List<Triangle>>() {});
    }

    protected void deleteTriangle(String triangleId) {
        given(createDefaultRequestSpec()).
                pathParam("triangleId", triangleId).
                delete("triangle/{triangleId}");
    }

    protected Triangle createTriangle(TriangleRequest requestPayload) {
        return given(createDefaultRequestSpec()).
                body(requestPayload).
                when().
                post("/triangle").
                then().
                statusCode(200).
                body("id", not(empty())).
                extract().
                as(Triangle.class);
    }

    protected void addTriangles(int numberOfTriangles){
        for (int i = 0;i < numberOfTriangles; i++) {

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
    }

    protected double triangleAre(double a,double b, double c){
        //https://en.wikipedia.org/wiki/Heron%27s_formula
        double s = (a + b + c);
        double A = Math.sqrt(s * (s - a) * (s - b) *  (s - c));
        return A;
    }

}

