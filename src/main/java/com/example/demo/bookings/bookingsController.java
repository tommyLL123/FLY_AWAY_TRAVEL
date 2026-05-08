package com.example.demo.bookings;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class bookingsController {

    private final bookingsService service;

    @GetMapping("/book/{id}")
    public bookings getBooking(@PathVariable String id) {
        return service.getBooking(id);
    }
}
