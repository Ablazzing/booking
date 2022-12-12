package org.example.booking;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.example.booking.dao.BookingDao;
import org.example.booking.dao.CustomerDao;
import org.example.booking.dao.RoomDao;
import org.example.booking.dto.BookingDtoRs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PositiveBookingControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    public void getBookingByCustomerByName() {
        BookingDtoRs expected = BookingDtoRs.builder()
                .number("111")
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customerName("Petr")
                .build();

        List<BookingDtoRs> actual = given()
                .accept(ContentType.JSON)
                .when()
                .get("/booking?customerEmail=222@gmail.com")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .body()
                .as(new TypeRef<List<BookingDtoRs>>() {
                });
        Assertions.assertEquals(expected, actual.get(0));
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void getBookingByCustomerByNumber() {
        BookingDtoRs expected = BookingDtoRs.builder()
                .number("111")
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customerName("Petr")
                .build();

        BookingDtoRs actual = given()
                .accept(ContentType.JSON)
                .when()
                .get("/booking?number=111")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .body()
                .as(new TypeRef<BookingDtoRs>() {
                });
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createBookingTest() {
        String inputBody = "{\n" +
                "    \"roomName\" : \"111\",\n" +
                "    \"startDate\" : \"2022-03-02\",\n" +
                "    \"endDate\" : \"2022-03-05\",\n" +
                "    \"customer\" :\n" +
                "    {\n" +
                "        \"email\" : \"test@mail.ru\",\n" +
                "        \"name\" : \"Roman\"\n" +
                "    }\n" +
                "}";
        String actual = given()
                .contentType(ContentType.JSON)
                .body(inputBody)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();
        Assertions.assertEquals(12, actual.length());
    }

    @Test
    public void deleteBookingTest() {
        String actual = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/booking/111")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .asString();
        Assertions.assertEquals("Успешно удалена бронь", actual);
    }
}

