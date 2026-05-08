package com.example.demo.bookings;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Long customerId;

    private String customerFirstName;

    private String customerLastName;

    private String flightId;

    private String flightNumber;

    private Date bookingDate;

    private Date estDepartureTime;

    private Date estArrivalTime;
}
