package com.example.demo.bookings;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class bookingsDTO {

    @NotBlank
    private String flightId;
}
