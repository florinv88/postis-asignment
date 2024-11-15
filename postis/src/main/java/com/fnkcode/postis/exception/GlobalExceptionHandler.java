package com.fnkcode.postis.exception;

import com.fnkcode.postis.dto.ErrorRequestDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            int dotIndex = fieldName.lastIndexOf(".");
            if (dotIndex != -1 && dotIndex < fieldName.length() - 1) {
                fieldName = fieldName.substring(dotIndex + 1);
            }
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


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
