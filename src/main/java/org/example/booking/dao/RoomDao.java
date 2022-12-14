package org.example.booking.dao;

import org.example.booking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoomDao extends JpaRepository<Room, Long> {

    Optional<Room> findFirstByName(String name);
}
