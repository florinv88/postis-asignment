package com.fnkcode.postis.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class VacationRequestDto {
    private String status;
    private LocalDate requestCreatedAt;
    private LocalDate vacationStartDate;
    private LocalDate vacationEndDate;
}
