package com.fnkcode.postis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
@Builder
public class ErrorRequestDTO {
    private HttpStatus httpStatus;
    private String errorMessage;
    private LocalDateTime errorTime;
}
