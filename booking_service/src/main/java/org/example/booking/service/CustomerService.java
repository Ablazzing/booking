package org.example.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.booking.dao.CustomerDao;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.mapper.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CustomerService {
    private final CustomerDao customerDao;
    private final BookingMapper bookingMapper;

    public List<BookingDtoRs> getBookingsByEmail(String customerEmail) {
        return customerDao.findFirstByEmail(customerEmail)
                .orElseThrow()
                .getBookings().stream()
                .map(bookingMapper::castFromEntity)
                .collect(Collectors.toList());
    }
}
