package com.fnkcode.postis.services;

import com.fnkcode.postis.dto.RequestResponseDTO;

public interface VacationRequestService {
    RequestResponseDTO getAllRequestsBasedOn(long id , String status);
}
