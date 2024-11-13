package com.fnkcode.postis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestResponseDTO {
   private String responseMsg;

   @JsonInclude(JsonInclude.Include.NON_EMPTY)
   private List<VacationRequestDto> vacationRequestList = new ArrayList<>();
}
