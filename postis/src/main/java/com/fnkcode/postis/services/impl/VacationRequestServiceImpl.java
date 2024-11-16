package com.fnkcode.postis.services.impl;

import com.fnkcode.postis.dto.ManagerRequestDTO;
import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.dto.VacationRequestDto;
import com.fnkcode.postis.entities.VacationRequest;
import com.fnkcode.postis.enums.RequestStatuses;
import com.fnkcode.postis.exception.InvalidDateRangeException;
import com.fnkcode.postis.exception.MaxDaysAllowedException;
import com.fnkcode.postis.exception.ResourceNotFoundException;
import com.fnkcode.postis.records.NewVacationRequest;
import com.fnkcode.postis.records.OverlappingRequests;
import com.fnkcode.postis.records.RequestDecision;
import com.fnkcode.postis.records.User;
import com.fnkcode.postis.repositories.VacationRequestRepository;
import com.fnkcode.postis.services.VacationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.fnkcode.postis.constants.MessageConstants.*;
import static com.fnkcode.postis.enums.RequestStatuses.PENDING;
import static com.fnkcode.postis.enums.RequestStatuses.getStatus;
import static com.fnkcode.postis.mapper.VacationRequestMapper.mapManagerRequestToDto;
import static com.fnkcode.postis.mapper.VacationRequestMapper.mapVacationRequestToDto;
import static com.fnkcode.postis.utils.DateUtils.parseDateDDMMYYYY;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationRequestServiceImpl implements VacationRequestService {

    private final VacationRequestRepository vacationRequestRepository;

    @Value("${max.allowed.days}")
    private long MAX_ALLOWED_DAYS;

    @Override
    public RequestResponseDTO getAllRequestsBasedOnAuthor(long id, String status) {

        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();

        RequestStatuses statusEnum = getStatus(status.toUpperCase());
        List<VacationRequest> vacationRequestList = vacationRequestRepository.findAllByAuthorAndStatus(id, statusEnum.getValue());

        if(!vacationRequestList.isEmpty()){
            requestResponseDTO.setResponseMsg(VACATION_REQUEST);
            requestResponseDTO.setVacationRequestList(getVacationRequests(vacationRequestList));
        } else {
            requestResponseDTO.setResponseMsg(NO_VACATION_REQUEST);
        }

        return requestResponseDTO;
    }

    private List<VacationRequestDto> getVacationRequests(List<VacationRequest> vacationRequestList){
        List<VacationRequestDto> vacationRequestDtoList = new ArrayList<>();
        for (VacationRequest vacationRequest : vacationRequestList) {
            VacationRequestDto vacationRequestDto = mapVacationRequestToDto(vacationRequest);
            vacationRequestDtoList.add(vacationRequestDto);
        }
        return vacationRequestDtoList;
    }

    @Override
    public RequestResponseDTO getNumberOfRemainingDays(long id) {
        long remainDays = remainDays(id);

        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();
        requestResponseDTO.setResponseMsg(NR_DAYS_REMAINING+remainDays);

        return requestResponseDTO;

    }

    private long remainDays(long id){
        long numberOfDaysTaken = vacationRequestRepository.getNumberOfVacationDaysTakenBy(id);
        return MAX_ALLOWED_DAYS-numberOfDaysTaken;
    }

    @Override
    @Transactional
    public RequestResponseDTO createVacationRequest(long id, NewVacationRequest vacationRequest) {
        long remainDays = remainDays(id);
        allowRequest(remainDays,vacationRequest);

        VacationRequest vacationReq = new VacationRequest();
        vacationReq.setAuthor(id);
        vacationReq.setStatus(PENDING.getValue());
        vacationReq.setVacationStartDate(vacationRequest.vacationStartDate());
        vacationReq.setVacationEndDate(vacationRequest.vacationEndDate());

        vacationRequestRepository.save(vacationReq);

        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();
        requestResponseDTO.setResponseMsg(SUCCESS);
        return requestResponseDTO;
    }

    private void allowRequest(long remainDays, NewVacationRequest vacationRequest) {

        if (vacationRequest.vacationStartDate().isAfter(vacationRequest.vacationEndDate())) {
            log.error("Start date is after the end date.");
            throw new InvalidDateRangeException(VACATION_DATES_ERR);
        }

        long freeDaysRequested = ChronoUnit.DAYS.between(vacationRequest.vacationStartDate(), vacationRequest.vacationEndDate());
        if (freeDaysRequested>remainDays){
            log.error("freeDaysRequested > remainDays");
            throw new MaxDaysAllowedException(VACATION_MAX_NUMBER_ERR);
        }
    }

    @Override
    public RequestResponseDTO getAllRequestsBasedOn(String status) {
        RequestStatuses statusEnum = getStatus(status.toUpperCase());
        List<VacationRequest> vacationRequestList = vacationRequestRepository.findAllByStatus(statusEnum.getValue());

        return mapEntityToDtoForManagerRequests(vacationRequestList);
    }

    @Override
    public RequestResponseDTO getAllRequestsBasedOnAuthor(String id) {
        //todo
        //aici ar merge mai intai o verificare de user in db
        //daca nu e UserNotFoundException
        List<VacationRequest> vacationRequestList = vacationRequestRepository.findAllByAuthor(Long.parseLong(id));

        return mapEntityToDtoForManagerRequests(vacationRequestList);
    }

    @Override
    @Transactional
    public RequestResponseDTO makeDecision(RequestDecision requestDecision, User user) {
        Long vacationId = Long.parseLong(requestDecision.vacationRequestId());
        Optional<VacationRequest> vacationRequestOpt = vacationRequestRepository.findById(vacationId);
        if (vacationRequestOpt.isEmpty()) {
            throw new ResourceNotFoundException(REQUEST_NOT_FOUND);
        }
        if (!vacationRequestOpt.get().getStatus().equals("0")) {
            throw new ResourceNotFoundException(REQUEST_NOT_UPDATEABLE);
        } else {
            VacationRequest vacationRequest = vacationRequestOpt.get();
            RequestStatuses status = getStatus(requestDecision.decision().toUpperCase());
            vacationRequest.setStatus(status.getValue());
            vacationRequest.setResolvedBy(user.id());
            vacationRequestRepository.save(vacationRequest);

        }
        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();
        requestResponseDTO.setResponseMsg(SUCCESS);
        return requestResponseDTO;
    }

    @Override
    public RequestResponseDTO getAllOverlappingRequests() {
        List<OverlappingRequests> overlappingRequests = vacationRequestRepository.findOverlappingRequests();
        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();
        requestResponseDTO.setResponseMsg(OVERLAPPING);
        requestResponseDTO.setOverlappingRequestsList(overlappingRequests);
        return requestResponseDTO;
    }

    private RequestResponseDTO mapEntityToDtoForManagerRequests(List<VacationRequest> vacationRequestList){
        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();
        if(!vacationRequestList.isEmpty()){
            requestResponseDTO.setResponseMsg(VACATION_REQUEST);
            requestResponseDTO.setRequestsList(getVacationRequestsByEmployee(vacationRequestList));
        } else {
            requestResponseDTO.setResponseMsg(NO_VACATION_REQUEST);
        }

        return requestResponseDTO;
    }

    private List<ManagerRequestDTO> getVacationRequestsByEmployee(List<VacationRequest> vacationRequestList){
        List<ManagerRequestDTO> vacationRequestDtoList = new ArrayList<>();
        for (VacationRequest vacationRequest : vacationRequestList) {
            ManagerRequestDTO vacationRequestDto = mapManagerRequestToDto(vacationRequest);
            vacationRequestDtoList.add(vacationRequestDto);
        }
        return vacationRequestDtoList;
    }

}
