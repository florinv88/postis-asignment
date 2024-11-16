package com.fnkcode.postis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fnkcode.postis.records.OverlappingRequests;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(
        name = "Response"
)
public class RequestResponseDTO {
   @Schema(
           name = "Response message"
   )
   private String responseMsg;

   @JsonInclude(JsonInclude.Include.NON_EMPTY)
   @Schema(
           name = "List of vacation request (if it's requested)"
   )
   private List<VacationRequestDto> vacationRequestList = new ArrayList<>();

   //used only by managers
   @JsonInclude(JsonInclude.Include.NON_EMPTY)
   @Schema(
           name = "List of managers requests (managers only)"
   )
   private List<ManagerRequestDTO> requestsList = new ArrayList<>();

   @JsonInclude(JsonInclude.Include.NON_EMPTY)
   @Schema(
           name = "List of overlapping requests (managers only)"
   )
   private List<OverlappingRequests> overlappingRequestsList = new ArrayList<>();
}
