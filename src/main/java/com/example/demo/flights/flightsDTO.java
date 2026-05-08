package com.example.demo.flights;
import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class flightsDTO {

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$")
    private String flightNumber;

    @NotBlank
    private String airlineName;

    @NotNull
    private Date estDepartureTime;

    @NotNull
    private Date estArrivalTime;

    @Min(value = 1)
    private int availableSeats;

    @Getter
    @Setter
    public static class CreateMany {

        @Valid
        @NotEmpty
        private List<flightsDTO> inputs;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateManyResponse {
        private List<String> ids;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchResponse {
        private List<flights> items;
    }
}
