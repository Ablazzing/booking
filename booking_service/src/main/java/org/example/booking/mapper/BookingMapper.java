package org.example.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.example.booking.dao.CustomerDao;
import org.example.booking.dao.RoomDao;
import org.example.booking.dto.BookingDtoRq;
import org.example.booking.dto.BookingDtoRs;
import org.example.booking.dto.CustomerDtoRq;
import org.example.booking.entity.Booking;
import org.example.booking.entity.Customer;
import org.example.booking.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingMapper {
    private final RoomDao roomDao;
    private final CustomerDao customerDao;

    public BookingDtoRs castFromEntity(Booking booking) {
        return BookingDtoRs.builder()
                .number(booking.getNumber())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .customerName(booking.getCustomer().getName())
                .roomName(booking.getRoom().getName())
                .build();
    }

    public Booking castFromDtoRq(BookingDtoRq bookingDtoRq) {
        CustomerDtoRq customerDtoRq = bookingDtoRq.getCustomer();
        Customer customer = customerDao.findFirstByEmail(customerDtoRq.getEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer(customerDtoRq.getName(), customerDtoRq.getEmail());
                    customerDao.save(newCustomer);
                    return newCustomer;
                });
        Room room = roomDao.findFirstByName(bookingDtoRq.getRoomName()).orElseThrow();
        String number = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        return Booking.builder()
                .customer(customer)
                .room(room)
                .number(number)
                .startDate(bookingDtoRq.getStartDate())
                .endDate(bookingDtoRq.getEndDate())
                .build();
    }
}
