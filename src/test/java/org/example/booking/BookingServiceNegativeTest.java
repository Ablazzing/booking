package org.example.booking;

import org.example.booking.dao.BookingDao;
import org.example.booking.dto.BookingDtoRq;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.dto.CustomerDtoRq;
import org.example.booking.mapper.BookingMapper;
import org.example.booking.service.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDate;

@SpringBootTest
public class BookingServiceNegativeTest {

    @MockBean
    private BookingDao bookingDao;

    @MockBean
    private BookingMapper bookingMapper;

    @Autowired
    private BookingService bookingService;

    @Test
    public void getBookingByNumberTest() {
        Mockito.when(bookingDao.findFirstByNumber(Mockito.any()))
                .thenReturn(null);
        Mockito.when(bookingMapper.castFromEntity(Mockito.any()))
                .thenReturn(BookingDtoRs.builder()
                        .number("111")
                        .roomName("111")
                        .customerName("Petr")
                        .startDate(LocalDate.of(2000, 1, 1))
                        .endDate(LocalDate.of(2000, 1, 2))
                        .build());
        Assertions.assertThrows(NullPointerException.class, () -> bookingService.getBookingByNumber("111"));
    }

    @Test
    public void createBookingNegativeTest() {
        Mockito.when(bookingDao.existsByStartDateAndEndDateAndRoom_Name(Mockito.any(),
                Mockito.any(),
                Mockito.any()))
                .thenReturn(true);
        BookingDtoRq bookingDtoRq = BookingDtoRq
                .builder()
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customer(CustomerDtoRq.builder().name("Petr").email("222@gamil.com").build())
                .build();
        Assertions.assertThrows(RuntimeException.class, () -> bookingService.createBooking(bookingDtoRq));
    }

    @Test
    public void deleteByNumberNegativeTest(){
        Mockito.when(bookingDao.deleteByNumber(Mockito.any())).thenReturn(-1);
        Assertions.assertEquals(false,bookingService.deleteByNumber("111"));
    }
}
