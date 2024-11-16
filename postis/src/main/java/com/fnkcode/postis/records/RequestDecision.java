package com.fnkcode.postis.records;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RequestDecision(
        @NotNull(message = "Vacation request id it's required.")
        @Pattern(regexp = "^[0-9]+$",message = "Vacation request id is invalid!")
        String vacationRequestId,
        @NotNull(message = "Decision it's required.")
        @Pattern(regexp = "approved|rejected")
        String decision) {
}
