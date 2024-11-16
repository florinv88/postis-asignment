package com.fnkcode.postis.service;

import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.records.NewVacationRequest;
import com.fnkcode.postis.records.RequestDecision;
import com.fnkcode.postis.records.User;

public interface VacationRequestService {
    RequestResponseDTO getAllRequestsBasedOnAuthor(long id , String status);
    RequestResponseDTO getNumberOfRemainingDays(long id);
    RequestResponseDTO createVacationRequest(long id, NewVacationRequest vacationRequest);
    RequestResponseDTO getAllRequestsBasedOn(String status);
    RequestResponseDTO getAllRequestsBasedOnAuthor(String id);
    RequestResponseDTO makeDecision(RequestDecision requestDecision, User user);
    RequestResponseDTO getAllOverlappingRequests();
}
