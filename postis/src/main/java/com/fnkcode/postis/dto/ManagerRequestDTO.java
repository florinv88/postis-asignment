package com.fnkcode.postis.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
public class ManagerRequestDTO {
    private long id;
    private long author;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long resolvedBy;
    private Date requestCreatedAt;
    private Date vacationStartDate;
    private Date vacationEndDate;
}
