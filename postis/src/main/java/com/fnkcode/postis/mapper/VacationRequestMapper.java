package com.fnkcode.postis.mapper;

import com.fnkcode.postis.dto.VacationRequestDto;
import com.fnkcode.postis.entities.VacationRequest;
import org.springframework.beans.BeanUtils;

public final class VacationRequestMapper {

    private VacationRequestMapper(){}

    public static VacationRequestDto mapVacationRequestToDto(VacationRequest vacationRequest){
        VacationRequestDto vacationRequestDto = new VacationRequestDto();
        BeanUtils.copyProperties(vacationRequest,vacationRequestDto);
        return vacationRequestDto;
    }
}
