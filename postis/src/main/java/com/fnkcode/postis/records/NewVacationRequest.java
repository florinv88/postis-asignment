package com.fnkcode.postis.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(
        name = "New vacation request"
)
public record NewVacationRequest(
        @NotNull(message = "Vacation start date it's required.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(
                description = "Start date", example = "15-05-2024"
        )
        LocalDate vacationStartDate,
        @NotNull(message = "Vacation end date it's required.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(
                description = "End date", example = "18-05-2024"
        )
        LocalDate vacationEndDate) {
}
