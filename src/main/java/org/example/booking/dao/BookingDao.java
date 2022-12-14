package org.example.booking.dao;

import org.example.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

public interface BookingDao extends JpaRepository<Booking, Long> {

    Optional<Booking> findFirstByNumber(String number);
    Boolean existsByStartDateAndEndDateAndRoom_Name(LocalDate startDate, LocalDate endDate, String name);

    Integer deleteByNumber(String number);
    Boolean removeByNumber(String number);
}
