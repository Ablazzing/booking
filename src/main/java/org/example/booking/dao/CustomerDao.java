package org.example.booking.dao;

import org.example.booking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer, Long> {

    Optional<Customer> findFirstByEmail(String email);
}
