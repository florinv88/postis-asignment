package com.fnkcode.postis.services.impl;

import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.dto.VacationRequestDto;
import com.fnkcode.postis.entities.VacationRequest;
import com.fnkcode.postis.enums.RequestStatuses;
import com.fnkcode.postis.repositories.VacationRequestRepository;
import com.fnkcode.postis.services.VacationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.fnkcode.postis.enums.RequestStatuses.getStatus;
import static com.fnkcode.postis.mapper.VacationRequestMapper.mapVacationRequestToDto;

@Service
@RequiredArgsConstructor
public class VacationRequestServiceImpl implements VacationRequestService {

    private final VacationRequestRepository vacationRequestRepository;

    @Override
    public RequestResponseDTO getAllRequestsBasedOn(long id, String status) {

        RequestResponseDTO requestResponseDTO = new RequestResponseDTO();

        RequestStatuses statusEnum = getStatus(status.toUpperCase());
        List<VacationRequest> vacationRequestList = vacationRequestRepository.findAllByAuthorAndStatus(id, statusEnum.getValue());

        if(!vacationRequestList.isEmpty()){
            requestResponseDTO.setResponseMsg("Your vacation requests that have status: "+status+" are:");
            List<VacationRequestDto> vacationRequestDtoList = new ArrayList<>();
            for (VacationRequest vacationRequest : vacationRequestList) {
                VacationRequestDto vacationRequestDto = mapVacationRequestToDto(vacationRequest);
                vacationRequestDtoList.add(vacationRequestDto);
            }
            requestResponseDTO.setVacationRequestList(vacationRequestDtoList);
        } else {
            requestResponseDTO.setResponseMsg("You don't have any vacation request that have status: "+status);
        }

        return requestResponseDTO;
    }
}
