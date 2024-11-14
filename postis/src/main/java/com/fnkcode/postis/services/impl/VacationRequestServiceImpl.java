package com.fnkcode.postis.services.impl;

import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.dto.VacationRequestDto;
import com.fnkcode.postis.entities.VacationRequest;
import com.fnkcode.postis.enums.RequestStatuses;
import com.fnkcode.postis.records.NewVacationRequest;
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

import static com.fnkcode.postis.constants.MessageConstants.*;
import static com.fnkcode.postis.enums.RequestStatuses.PENDING;
import static com.fnkcode.postis.enums.RequestStatuses.getStatus;
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
            requestResponseDTO.setResponseMsg(VACATION_REQUEST+status);
            //TODO de extras intr-o metoda separata
            List<VacationRequestDto> vacationRequestDtoList = new ArrayList<>();
            for (VacationRequest vacationRequest : vacationRequestList) {
                VacationRequestDto vacationRequestDto = mapVacationRequestToDto(vacationRequest);
                vacationRequestDtoList.add(vacationRequestDto);
            }
            requestResponseDTO.setVacationRequestList(vacationRequestDtoList);
        } else {
            requestResponseDTO.setResponseMsg(NO_VACATION_REQUEST+status);
        }

        return requestResponseDTO;
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
}
