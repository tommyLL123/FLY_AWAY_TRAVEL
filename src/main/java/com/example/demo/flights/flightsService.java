package com.example.demo.flights;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class flightsService {

    private final flightsRepository repo;

    public flights createFlight(flightsDTO dto) {

        if (repo.existsByFlightNumber(dto.getFlightNumber())) {
            throw new RuntimeException("El número de vuelo ya existe");
        }

        if (!dto.getEstDepartureTime().before(dto.getEstArrivalTime())) {
            throw new RuntimeException("La hora de salida debe ser menor a la de llegada");
        }

        flights flight = new flights();
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setAirlineName(dto.getAirlineName());
        flight.setEstDepartureTime(dto.getEstDepartureTime());
        flight.setEstArrivalTime(dto.getEstArrivalTime());
        flight.setAvailableSeats(dto.getAvailableSeats());

        return repo.save(flight);
    }

    public List<String> createMany(flightsDTO.CreateMany dto) {
        List<String> ids = new ArrayList<>();
        for (flightsDTO input : dto.getInputs()) {
            ids.add(createFlight(input).getId());
        }
        return ids;
    }

    public flightsDTO.SearchResponse searchFlights(String flightNumber, String airlineName, String startDate, String endDate) {
        Stream<flights> flightsStream = repo.findAll().stream();

        if (flightNumber != null && !flightNumber.isBlank()) {
            String cleanFlightNumber = flightNumber.toLowerCase();
            flightsStream = flightsStream.filter(flight -> flight.getFlightNumber().toLowerCase().contains(cleanFlightNumber));
        }

        if (airlineName != null && !airlineName.isBlank()) {
            String cleanAirline = airlineName.toLowerCase();
            flightsStream = flightsStream.filter(flight -> flight.getAirlineName() != null
                    && flight.getAirlineName().toLowerCase().contains(cleanAirline));
        }

        Date parsedStartDate = parseDate(startDate, false);
        Date parsedEndDate = parseDate(endDate, true);

        if (parsedStartDate != null) {
            flightsStream = flightsStream.filter(flight -> !flight.getEstDepartureTime().before(parsedStartDate));
        }

        if (parsedEndDate != null) {
            flightsStream = flightsStream.filter(flight -> !flight.getEstDepartureTime().after(parsedEndDate));
        }

        return new flightsDTO.SearchResponse(flightsStream.sorted(Comparator.comparing(flights::getFlightNumber)).toList());
    }

    private Date parseDate(String value, boolean endOfDay) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Date.from(Instant.parse(value));
        } catch (Exception ignored) {
            LocalDate localDate = LocalDate.parse(value);
            if (endOfDay) {
                return Date.from(localDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).minusNanos(1).toInstant());
            }
            return Date.from(localDate.atStartOfDay(ZoneOffset.UTC).toInstant());
        }
    }
}
