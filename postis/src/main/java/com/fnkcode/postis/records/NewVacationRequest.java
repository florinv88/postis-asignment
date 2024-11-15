package com.fnkcode.postis.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewVacationRequest(
        @NotNull(message = "Vacation start date it's required.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate vacationStartDate,
        @NotNull(message = "Vacation end date it's required.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate vacationEndDate) {
}
