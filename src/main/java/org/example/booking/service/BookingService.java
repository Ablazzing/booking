package org.example.booking.service;

import lombok.RequiredArgsConstructor;
import org.example.booking.dao.BookingDao;
import org.example.booking.dto.BookingDtoRq;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.entity.Booking;
import org.example.booking.mapper.BookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingService {
    private final BookingDao bookingDao;
    private final BookingMapper bookingMapper;

    public Optional<BookingDtoRs> getBookingByNumber(String number) {
        return bookingDao.findFirstByNumber(number).map(bookingMapper::castFromEntity);
    }

    @Transactional
    public String createBooking(BookingDtoRq bookingDtoRq) {
        if (bookingDao.existsByStartDateAndEndDateAndRoom_Name(bookingDtoRq.getStartDate(),
                bookingDtoRq.getEndDate(), bookingDtoRq.getRoomName())) {
            throw new RuntimeException("Уже есть бронь");
        }
        Booking booking = bookingMapper.castFromDtoRq(bookingDtoRq);
        bookingDao.save(booking);
        return booking.getNumber();
    }

    @Transactional
    public Boolean deleteByNumber(String number) {
        return bookingDao.deleteByNumber(number) > 0;
    }
}
