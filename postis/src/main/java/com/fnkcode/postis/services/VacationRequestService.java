package com.fnkcode.postis.services;

import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.records.NewVacationRequest;

public interface VacationRequestService {
    RequestResponseDTO getAllRequestsBasedOn(long id , String status);
    RequestResponseDTO getNumberOfRemainingDays(long id);
    RequestResponseDTO createVacationRequest(long id, NewVacationRequest vacationRequest);
}
