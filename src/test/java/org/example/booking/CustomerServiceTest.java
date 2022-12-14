package org.example.booking;

import org.example.booking.dao.CustomerDao;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.entity.Customer;
import org.example.booking.mapper.BookingMapper;
import org.example.booking.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerDao customerDao;

    @MockBean
    private BookingMapper bookingMapper;

    @Test
    public void getBookingsByEmailTest() {
        Mockito.when(customerDao.findFirstByEmail(Mockito.any()))
                .thenReturn(Optional.of(new Customer("Petr", "222@gmail.com")));
        Mockito.when(bookingMapper.castFromEntity(Mockito.any())).thenReturn(BookingDtoRs
                .builder()
                .number("111")
                .roomName("111")
                .startDate(LocalDate.of(2000, 1, 1))
                .endDate(LocalDate.of(2000, 1, 2))
                .customerName("Petr")
                .build());
        Assertions.assertThrows(NullPointerException.class,
                () -> customerService.getBookingsByEmail("222@gmail.com"));
    }
}

