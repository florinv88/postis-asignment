package com.fnkcode.postis.services.impl;

import com.fnkcode.postis.dto.ManagerRequestDTO;
import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.dto.VacationRequestDto;
import com.fnkcode.postis.entities.VacationRequest;
import com.fnkcode.postis.enums.RequestStatuses;
import com.fnkcode.postis.records.NewVacationRequest;
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
    public RequestResponseDTO getAllRequestsBasedOn(long id, String status) {

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
        vacationReq.setVacationStartDate(parseDateDDMMYYYY(vacationRequest.vacationStartDate()));
        vacationReq.setVacationEndDate(parseDateDDMMYYYY(vacationRequest.vacationEndDate()));

        vacationRequestRepository.save(vacationReq);

        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();
        requestResponseDTO.setResponseMsg(SUCCESS);
        return requestResponseDTO;
    }

    private void allowRequest(long remainDays, NewVacationRequest vacationRequest) {
        //todo de implementat global err handler
        Date startDate = parseDateDDMMYYYY(vacationRequest.vacationStartDate());
        Date endDate = parseDateDDMMYYYY(vacationRequest.vacationEndDate());

        //if statDate > endDate -- err (de implementat)

        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long freeDaysRequested = ChronoUnit.DAYS.between(localStartDate, localEndDate);
        if (freeDaysRequested>remainDays){
            // eroare global handler
            log.error("freeDaysRequested > remainDays");
        }
    }

    @Override
    public RequestResponseDTO getAllRequestsBasedOn(String status) {
        RequestStatuses statusEnum = getStatus(status.toUpperCase());
        List<VacationRequest> vacationRequestList = vacationRequestRepository.findAllByStatus(statusEnum.getValue());

        return mapEntityToDtoForManagerRequests(vacationRequestList);
    }

    @Override
    public RequestResponseDTO getAllRequestsBasedOn(long id) {
        List<VacationRequest> vacationRequestList = vacationRequestRepository.findAllByAuthor(id);

        return mapEntityToDtoForManagerRequests(vacationRequestList);
    }

    @Override
    @Transactional
    public Boolean isUpdated(RequestDecision requestDecision, User user) {
        Optional<VacationRequest> vacationRequestOpt = vacationRequestRepository.findById(requestDecision.vacationRequestId());
        if(vacationRequestOpt.isEmpty() ||  !vacationRequestOpt.get().getStatus().equals("0")) {
            //altfel err handler
            return Boolean.FALSE;
        } else {
            VacationRequest vacationRequest = vacationRequestOpt.get();
            RequestStatuses status = getStatus(requestDecision.decision().toUpperCase());
            vacationRequest.setStatus(status.getValue());
            vacationRequest.setResolvedBy(user.id());
            vacationRequestRepository.save(vacationRequest);
        }

        return Boolean.TRUE;
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
