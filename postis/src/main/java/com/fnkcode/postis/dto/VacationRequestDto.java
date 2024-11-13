package com.fnkcode.postis.dto;


import lombok.Data;

import java.util.Date;

@Data
public class VacationRequestDto {
    private long author;
    private String status;
    private Date requestCreatedAt;
    private Date vacationStartDate;
    private Date vacationEndDate;
}
