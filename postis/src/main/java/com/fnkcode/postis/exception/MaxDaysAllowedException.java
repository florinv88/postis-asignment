package com.fnkcode.postis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class MaxDaysAllowedException extends RuntimeException {

    public MaxDaysAllowedException(String message){
        super(message);
    }

}
