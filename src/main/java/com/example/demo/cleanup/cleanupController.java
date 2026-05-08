package com.example.demo.cleanup;

import com.example.demo.bookings.bookingsRepository;
import com.example.demo.flights.flightsRepository;
import com.example.demo.users.usersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class cleanupController {

    private final bookingsRepository bookingsRepository;
    private final flightsRepository flightsRepository;
    private final usersRepository usersRepository;

    @DeleteMapping("/cleanup")
    public void cleanup() {
        bookingsRepository.deleteAll();
        flightsRepository.deleteAll();
        usersRepository.deleteAll();
    }
}
