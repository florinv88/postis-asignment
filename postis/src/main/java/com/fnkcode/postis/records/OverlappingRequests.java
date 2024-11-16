package com.fnkcode.postis.records;

import com.fnkcode.postis.dto.ManagerRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "Overlapping Request"
)
public record OverlappingRequests(ManagerRequestDTO vacationRequest1,
                                  ManagerRequestDTO vacationRequest2) {
}
