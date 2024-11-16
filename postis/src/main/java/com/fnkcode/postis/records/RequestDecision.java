package com.fnkcode.postis.records;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "Manager's Decision"
)
public record RequestDecision(
        @Schema(
                description = "Vacation request id"
        )
        @NotNull(message = "Vacation request id it's required.")
        @Pattern(regexp = "^[0-9]+$",message = "Vacation request id is invalid!")
        String vacationRequestId,

        @Schema(
                description = "Decision", example = "approved / rejected"
        )
        @NotNull(message = "Decision it's required.")
        @Pattern(regexp = "approved|rejected")
        String decision) {
}
