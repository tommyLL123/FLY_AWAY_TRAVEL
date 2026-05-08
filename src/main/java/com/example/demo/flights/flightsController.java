package com.example.demo.flights;

import com.example.demo.bookings.bookings;
import com.example.demo.bookings.bookingsDTO;
import com.example.demo.bookings.bookingsService;
import com.example.demo.common.newIdDTO;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class flightsController {

    private final flightsService service;
    private final bookingsService bookingsService;

    @PostMapping("/create")
    public ResponseEntity<newIdDTO> createFlight(@Valid @RequestBody flightsDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new newIdDTO(service.createFlight(dto).getId()));
    }

    @PostMapping("/create-many")
    public ResponseEntity<flightsDTO.CreateManyResponse> createMany(@Valid @RequestBody flightsDTO.CreateMany dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new flightsDTO.CreateManyResponse(service.createMany(dto)));
    }

    @GetMapping("/search")
    public flightsDTO.SearchResponse searchFlights(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String departureStart,
            @RequestParam(required = false) String departureEnd,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo) {
        if (startDate == null) {
            startDate = departureStart != null ? departureStart : estDepartureTimeFrom;
        }
        if (endDate == null) {
            endDate = departureEnd != null ? departureEnd : estDepartureTimeTo;
        }
        return service.searchFlights(flightNumber, airlineName, startDate, endDate);
    }

    @PostMapping("/book")
    public newIdDTO bookFlight(@Valid @RequestBody bookingsDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String firstName = (String) request.getAttribute("firstName");
        String lastName = (String) request.getAttribute("lastName");
        bookings booking = bookingsService.bookFlight(dto, userId, firstName, lastName);
        return new newIdDTO(booking.getId());
    }

    @GetMapping("/book/{id}")
    public bookings getBookingFromFlightsPath(@PathVariable String id) {
        return bookingsService.getBooking(id);
    }
}
