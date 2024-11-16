package com.fnkcode.postis.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Manager Request"
)
public class ManagerRequestDTO {
    @Schema(
            name = "Id Request"
    )
    private long id;
    @Schema(
            name = "Request author"
    )
    private long author;
    @Schema(
            name = "Request status"
    )
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(
            name = "Manager ID"
    )
    private Long resolvedBy;
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
