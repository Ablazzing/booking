package org.example.booking;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NegativeBookingControllerTest {

    @LocalServerPort
    private int port;

    private final InitializerBdService initializerBdService;

    @Autowired
    public NegativeBookingControllerTest(InitializerBdService initializerBdService) {
        this.initializerBdService = initializerBdService;
    }

    @BeforeEach
    public void init(){
        initializerBdService.init();
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    public void destroy(){
        initializerBdService.destroy();
    }

    @Test
    public void negativeGetBookingByCustomerByNumber() {
        String actual = given()
                .accept(ContentType.JSON)
                .when()
                .get("/booking?number=11223")
                .then()
                .statusCode(400)
                .extract()
                .response()
                .body().asString();
        Assertions.assertEquals("No value present", actual);
    }

    @Test
    public void negativeCreateBooking() {
        String inputBody = "{\n" +
                "    \"roomName\" : \"111\",\n" +
                "    \"startDate\" : \"2000-01-01\",\n" +
                "    \"endDate\" : \"2000-01-02\",\n" +
                "    \"customer\" :\n" +
                "    {\n" +
                "        \"email\" : \"222@gmail.com\",\n" +
                "        \"name\" : \"Petr\"\n" +
                "    }\n" +
                "}";
        String actual = given()
                .contentType(ContentType.JSON)
                .body(inputBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(400)
                .extract()
                .response()
                .asString();
        Assertions.assertEquals("Уже есть бронь", actual);
    }

    @Test
    public void negativeDeleteBooking() {
        String actual = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/booking/1123")
                .then()
                .statusCode(400)
                .extract()
                .response()
                .asString();
        Assertions.assertEquals("Такого бронирования - нет", actual);
    }
}
