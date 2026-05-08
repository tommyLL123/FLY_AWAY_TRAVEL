package com.example.demo.flights;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface flightsRepository extends JpaRepository<flights, String> {
    boolean existsByFlightNumber(String flightNumber);

    List<flights> findByFlightNumberContainingIgnoreCase(String flightNumber);

    List<flights> findByEstDepartureTimeBetween(Date startDate, Date endDate);
}
