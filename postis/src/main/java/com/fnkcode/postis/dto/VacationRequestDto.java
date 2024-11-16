package com.fnkcode.postis.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Schema(
        name = "Vacation Request"
)
public class VacationRequestDto {
    @Schema(
            name = "Status request"
    )
    private String status;
    @Schema(
            name = "Time of request creation"
    )
    private Date requestCreatedAt;
    @Schema(
            name = "Vacation request start date"
    )
    private LocalDate vacationStartDate;
    @Schema(
            name = "Vacation request end date"
    )
    private LocalDate vacationEndDate;
}
