package org.example.booking;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.example.booking.dto.BookingDtoRs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest
public class NegativeBookingControllerTest {

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
