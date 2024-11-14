package com.fnkcode.postis.mapper;

import com.fnkcode.postis.dto.VacationRequestDto;
import com.fnkcode.postis.entities.VacationRequest;
import org.springframework.beans.BeanUtils;

import static com.fnkcode.postis.enums.RequestStatuses.getStatusByValue;

public final class VacationRequestMapper {

    private VacationRequestMapper(){}

    public static VacationRequestDto mapVacationRequestToDto(VacationRequest vacationRequest){
        VacationRequestDto vacationRequestDto = new VacationRequestDto();
        BeanUtils.copyProperties(vacationRequest,vacationRequestDto,"status");
        vacationRequestDto.setStatus(getStatusByValue(vacationRequest.getStatus()).getName());
        return vacationRequestDto;
    }
}
