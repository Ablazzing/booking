package org.example.booking;

import org.example.booking.dao.BookingDao;
import org.example.booking.dto.BookingDtoRq;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.dto.CustomerDtoRq;
import org.example.booking.entity.Booking;
import org.example.booking.entity.Customer;
import org.example.booking.entity.Room;
import org.example.booking.entity.RoomLevel;
import org.example.booking.mapper.BookingMapper;
import org.example.booking.service.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class BookingServiceTest {

    @MockBean
    private BookingDao bookingDao;

    @MockBean
    private BookingMapper bookingMapper;

    @Autowired
    private BookingService bookingService;

    private final Customer customer = new Customer("Petr", "222@gmail.com");
    private final Room room = new Room("111", RoomLevel.ECONOM);
    private final Booking booking = new Booking("111", LocalDate.of(2000, 1, 1),
            LocalDate.of(2000, 1, 2),
            room,
            customer);

    @Test
    public void getBookingByNumberTest() {
        Mockito.when(bookingDao.findFirstByNumber(Mockito.any()))
                .thenReturn(Optional.of(booking));
        Mockito.when(bookingMapper.castFromEntity(Mockito.any()))
                .thenReturn(BookingDtoRs.builder()
                        .number("111")
                        .roomName("111")
                        .customerName("Petr")
                        .startDate(LocalDate.of(2000, 1, 1))
                        .endDate(LocalDate.of(2000, 1, 2))
                        .build());
        Optional<BookingDtoRs> actual = Optional.of(BookingDtoRs.builder().number("111")
                .roomName("111")
                .customerName("Petr")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .build());
        Assertions.assertEquals(bookingService.getBookingByNumber("111"), actual);
    }

    @Test
    public void createBookingTest() {
        Mockito.when(bookingDao.existsByStartDateAndEndDateAndRoom_Name(Mockito.any(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(false);
        Mockito.when(bookingMapper.castFromDtoRq(Mockito.any()))
                .thenReturn(booking);
        BookingDtoRq bookingDtoRq = BookingDtoRq
                .builder()
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customer(CustomerDtoRq.builder().name("Petr").email("222@gamil.com").build())
                .build();
        Assertions.assertEquals("111", bookingService.createBooking(bookingDtoRq));
    }

    @Test
    public void deleteByNumberTest(){
        Mockito.when(bookingDao.deleteByNumber(Mockito.any())).thenReturn(1);
        Assertions.assertEquals(true,bookingService.deleteByNumber("111"));
    }
}
