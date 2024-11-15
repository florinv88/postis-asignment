package com.fnkcode.postis.records;

import com.fnkcode.postis.dto.ManagerRequestDTO;

public record OverlappingRequests(ManagerRequestDTO vacationRequest1,
                                  ManagerRequestDTO vacationRequest2) {
}
