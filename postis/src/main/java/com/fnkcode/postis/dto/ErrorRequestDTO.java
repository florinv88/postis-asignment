package com.fnkcode.postis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
@Builder
@Schema(
        name = "Error Request"
)
public class ErrorRequestDTO {
    private HttpStatus httpStatus;
    private String errorMessage;
    private LocalDateTime errorTime;
}
