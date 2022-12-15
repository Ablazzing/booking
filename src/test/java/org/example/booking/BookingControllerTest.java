package org.example.booking;


import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.example.booking.dao.BookingDao;
import org.example.booking.dao.CustomerDao;
import org.example.booking.dao.RoomDao;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.entity.Booking;
import org.example.booking.entity.Customer;
import org.example.booking.entity.Room;
import org.example.booking.entity.RoomLevel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingControllerTest {

    private final BookingDao bookingDao;
    private final CustomerDao customerDao;
    private final RoomDao roomDao;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        baseURI = "http://localhost:" + port;
    }

    @PostConstruct
    public void fillDb() {
        Customer bobby = customerDao.save(new Customer("Bobby", "bob@ya.ru"));
        Room room = roomDao.save(new Room("222", RoomLevel.ECONOM));
        bookingDao.save(new Booking("abc1", LocalDate.of(2022, 12, 01),
                LocalDate.of(2022, 12, 05), room, bobby));
    }

    @Nested
    public class CreateBookingTest {

        @Test
        @DisplayName("Успешное создание брони")
        @DirtiesContext
        void createBookingIT() {
            String inputBody = "{\n" +
                    "  \"roomName\": \"222\",\n" +
                    "  \"startDate\":\"2022-12-10\",\n" +
                    "  \"endDate\":\"2022-12-15\",\n" +
                    "  \"customer\":  {\n" +
                    "      \"name\": \"Sam\",\n" +
                    "       \"email\": \"sam@ya.ru\"\n" +
                    "    }\n" +
                    "}";
            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(inputBody)
                    .when()
                    .post("/booking")
                    .then()
                    .extract()
                    .response();
            Assertions.assertEquals(200, response.statusCode());
            Optional<Booking> byId = bookingDao.findById(2L);
            String expected = byId.get().getNumber();
            String actual = response.body().asString();
            Assertions.assertEquals(expected, actual);
        }

        @Test
        @DisplayName("Неуспешное создание брони")
        @DirtiesContext
        void createBookingFailIT() {
            String inputBody = "{\n" +
                    "  \"roomName\": \"222\",\n" +
                    "  \"startDate\":\"2022-12-01\",\n" +
                    "  \"endDate\":\"2022-12-05\",\n" +
                    "  \"customer\":  {\n" +
                    "      \"name\": \"Jack\",\n" +
                    "       \"email\": \"jackb@ya.ru\"\n" +
                    "    }\n" +
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
            String expected = "Уже есть бронь";
            String actual = response.body().asString();
            Assertions.assertEquals(expected, actual);
        }
    }

    @Nested
    public class GetBookingTest {

        @Test()
        @DisplayName("Успешное получение бронирований по email")
        @DirtiesContext
        public void getBookingsByCustomerEmailIT() {
            BookingDtoRs expected = BookingDtoRs.builder()
                    .number("abc1")
                    .startDate(LocalDate.of(2022, 12, 01))
                    .endDate(LocalDate.of(2022, 12, 05))
                    .customerName("Bobby")
                    .roomName("222")
                    .build();
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/booking?customerEmail=bob@ya.ru")
                    .then()
                    .extract()
                    .response();
            List<BookingDtoRs> bookingDtoRs = response.body().as(new TypeRef<>() {
            });
            Assertions.assertEquals(1, bookingDtoRs.size());
            Assertions.assertEquals(expected, bookingDtoRs.get(0));
        }

        @Test()
        @DisplayName("Неуспешное получение бронирований по email")
        @DirtiesContext
        public void getBookingsByCustomerEmailFailIT() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/booking?customerEmail=failtest@ya.ru")
                    .then()
                    .extract()
                    .response();
            Assertions.assertEquals(400, response.statusCode());
        }

        @Test()
        @DisplayName("Успешное получение бронирований по номеру брони")
        @DirtiesContext
        public void getBookingsByNumberIT() {
            BookingDtoRs expected = BookingDtoRs.builder()
                    .number("abc1")
                    .startDate(LocalDate.of(2022, 12, 01))
                    .endDate(LocalDate.of(2022, 12, 05))
                    .customerName("Bobby")
                    .roomName("222")
                    .build();
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/booking?number=abc1")
                    .then()
                    .extract()
                    .response();
            BookingDtoRs actual = response.body().as(BookingDtoRs.class);
            Assertions.assertEquals(expected, actual);
        }

        @Test()
        @DisplayName("Неуспешное получение бронирований по номеру брони")
        @DirtiesContext
        public void getBookingsByNumberFailIT() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/booking?number=failNumber")
                    .then()
                    .extract()
                    .response();
            Assertions.assertEquals(400, response.statusCode());
        }
    }

    @Nested
    public class DeleteBookingTest {

        @Test
        @DisplayName("Успешное удаление брони по номеру")
        @DirtiesContext
        public void deleteBookingIT() {
            Customer victor = customerDao.save(new Customer("Victor", "victor@ya.ru"));
            Room room = roomDao.save(new Room("555", RoomLevel.ECONOM));
            Booking booking = bookingDao.save(new Booking("acc123", LocalDate.of(2022, 12, 06),
                    LocalDate.of(2022, 12, 07), room, victor));
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/booking/acc123")
                    .then()
                    .extract()
                    .response();
            Assertions.assertEquals(200, response.statusCode());
            Optional<Booking> actual = bookingDao.findFirstByNumber("acc123");
            Assertions.assertTrue(actual.isEmpty());
        }

        @Test
        @DisplayName("Неуспешное удаление брони по номеру")
        @DirtiesContext
        public void deleteBookingFailIT() {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete("/booking/aaaaa")
                    .then()
                    .extract()
                    .response();
            Assertions.assertEquals(400, response.statusCode());
        }
    }

}

