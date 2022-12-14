package org.example.booking;

import org.example.booking.dao.BookingDao;
import org.example.booking.dao.CustomerDao;
import org.example.booking.dao.RoomDao;
import org.example.booking.entity.Booking;
import org.example.booking.entity.Customer;
import org.example.booking.entity.Room;
import org.example.booking.entity.RoomLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class InitializerBdService {

    private CustomerDao customerDao;

    private RoomDao roomDao;

    private BookingDao bookingDao;

    @Autowired
    public InitializerBdService(CustomerDao customerDao, RoomDao roomDao, BookingDao bookingDao) {
        this.customerDao = customerDao;
        this.roomDao = roomDao;
        this.bookingDao = bookingDao;
    }

    @Transactional
    public void init() {
        Customer customer = new Customer("Petr", "222@gmail.com");
        customerDao.save(customer);
        Room room = new Room("111", RoomLevel.ECONOM);
        roomDao.save(room);
        Booking booking = new Booking("111", LocalDate.of(2000, 1, 1),
                LocalDate.of(2000, 1, 2),
                room,
                customer);
        bookingDao.save(booking);
    }

    @Transactional
    public void destroy() {
        bookingDao.deleteAll();
        roomDao.deleteAll();
        customerDao.deleteAll();
    }
}
