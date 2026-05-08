package com.example.demo.bookings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface bookingsRepository extends JpaRepository<bookings, String> {
    List<bookings> findByCustomerId(Long customerId);

    boolean existsByCustomerIdAndFlightNumber(Long customerId, String flightNumber);

    void deleteByFlightNumber(String flightNumber);

    List<bookings> findByCustomerIdAndEstDepartureTimeBeforeAndEstArrivalTimeAfter(Long customerId, Date arrivalTime, Date departureTime);
}
