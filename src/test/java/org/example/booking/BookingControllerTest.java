package org.example.booking;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.booking.dto.BookingDtoRs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        baseURI = "http://localhost:" + port;
    }

    @Test
    public void getBookingsByCustomer() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?customerEmail=222@gmail.com")
                .then()
                .extract()
                .response();

        Response response2 = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?number=111")
                .then()
                .extract()
                .response();

        List<BookingDtoRs> actual = response.body().as(new TypeRef<List<BookingDtoRs>>() {});
        BookingDtoRs actual2 = response2.body().as(new TypeRef<BookingDtoRs>() {});

        BookingDtoRs expected = BookingDtoRs. builder()
                .number("111")
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customerName("Petr")
                .build();

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(expected, actual.get(0));
        Assertions.assertEquals(expected, actual2);
    }

    @Test
    public void getBookingsByCustomerWithException() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?customerEmail=333@gmail.com")
                .then()
                .extract()
                .response();

        Response response2 = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/booking?number=555")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertEquals(400, response2.statusCode());
    }

    @Test
    public void createBooking() {
        String inputBody = "{\"roomName\": \"111\",\n" +
                " \"startDate\": \"2000-02-02\",\n" +
                " \"endDate\": \"2000-02-03\",\n" +
                " \"customer\" :\n" +
                " \t\t{\n" +
                " \t\t\t\"email\": \"444@gmail.com\",\n" +
                "      \"name\": \"Yuri\"\n" +
                "\t}\n" +
                "}";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(inputBody)
                .when()
                .post("/booking")
                .then()
                .extract()
                .response();

        String actual = response.body().asString();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(12, actual.length());
    }

    @Test
    public void createBookingWithException() {
        String inputBody = "{\"roomName\": \"111\",\n" +
                " \"startDate\": \"2000-01-01\",\n" +
                " \"endDate\": \"2000-01-02\",\n" +
                " \"customer\" :\n" +
                " \t\t{\n" +
                " \t\t\t\"email\": \"222@gmail.com\",\n" +
                "      \"name\": \"Petr\"\n" +
                "\t}\n" +
                "}";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(inputBody)
                .when()
                .post("/booking")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(400, response.statusCode());
    }

    @Test
    public void deleteBooking() {

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/booking/111")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void deleteBookingWithException() {

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/booking/547")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(400, response.statusCode());
    }
}
