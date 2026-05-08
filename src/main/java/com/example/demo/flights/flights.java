package com.example.demo.flights;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class flights {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$")
    @Column(unique = true, nullable = false)
    private String flightNumber;

    @NotBlank
    private String airlineName;

    @NotNull
    private Date estDepartureTime;

    @NotNull
    private Date estArrivalTime;

    private int availableSeats;
}
