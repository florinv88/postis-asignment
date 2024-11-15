package com.fnkcode.postis.exception;

import com.fnkcode.postis.dto.ErrorRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorRequestDTO> handleResourceNotFoundException(ResourceNotFoundException exception){

        ErrorRequestDTO errorRequestDTO = ErrorRequestDTO.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorRequestDTO);
    }

    @ExceptionHandler(MaxDaysAllowedException.class)
    public ResponseEntity<ErrorRequestDTO> handleMaxDaysAllowedException(MaxDaysAllowedException exception){

        ErrorRequestDTO errorRequestDTO = ErrorRequestDTO.builder()
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(errorRequestDTO);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorRequestDTO> handleInvalidDateRangeException(InvalidDateRangeException exception){

        ErrorRequestDTO errorRequestDTO = ErrorRequestDTO.builder()
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(errorRequestDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRequestDTO> handleGlobalException(Exception exception) {
        ErrorRequestDTO errorRequestDTO = ErrorRequestDTO.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorRequestDTO);
    }

}
