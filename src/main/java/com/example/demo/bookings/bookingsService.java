package com.example.demo.bookings;

import com.example.demo.flights.flights;
import com.example.demo.flights.flightsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class bookingsService {

    private static final DateTimeFormatter ISO_MILLIS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .withZone(java.time.ZoneOffset.UTC);

    private final bookingsRepository bookingsRepository;
    private final flightsRepository flightsRepository;

    public bookings bookFlight(bookingsDTO dto, Long customerId, String firstName, String lastName) {
        flights flight = flightsRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No hay asientos disponibles");
        }

        if (!flight.getEstDepartureTime().after(Date.from(Instant.now())) || !flight.getEstArrivalTime().after(Date.from(Instant.now()))) {
            throw new RuntimeException("No se puede reservar un vuelo pasado o en transito");
        }

        if (bookingsRepository.existsByCustomerIdAndFlightNumber(customerId, flight.getFlightNumber())) {
            throw new RuntimeException("El usuario ya tiene una reserva para este vuelo");
        }

        boolean hasConflict = !bookingsRepository
                .findByCustomerIdAndEstDepartureTimeBeforeAndEstArrivalTimeAfter(customerId, flight.getEstArrivalTime(), flight.getEstDepartureTime())
                .isEmpty();
        if (hasConflict) {
            throw new RuntimeException("Existe una reserva con conflicto de horario");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightsRepository.save(flight);

        bookings booking = new bookings();
        booking.setCustomerId(customerId);
        booking.setCustomerFirstName(firstName);
        booking.setCustomerLastName(lastName);
        booking.setFlightId(flight.getId());
        booking.setFlightNumber(flight.getFlightNumber());
        booking.setBookingDate(new Date());
        booking.setEstDepartureTime(flight.getEstDepartureTime());
        booking.setEstArrivalTime(flight.getEstArrivalTime());

        bookings savedBooking = bookingsRepository.save(booking);
        saveConfirmationEmail(savedBooking);
        return savedBooking;
    }

    public bookings getBooking(String id) {
        return bookingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    private void saveConfirmationEmail(bookings booking) {
        String content = "Reserva #" + booking.getId() + System.lineSeparator()
                + "Nombre: " + booking.getCustomerFirstName() + " " + booking.getCustomerLastName() + System.lineSeparator()
                + "Numero de vuelo: " + booking.getFlightNumber() + System.lineSeparator()
                + "Fecha de reserva: " + formatDate(booking.getBookingDate()) + System.lineSeparator()
                + "Salida: " + formatDate(booking.getEstDepartureTime()) + System.lineSeparator()
                + "Llegada: " + formatDate(booking.getEstArrivalTime());
        try {
            Files.writeString(Path.of("flight_booking_email_" + booking.getId() + ".txt"), content);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el email de confirmacion");
        }
    }

    private String formatDate(Date date) {
        return ISO_MILLIS.format(date.toInstant());
    }
}
