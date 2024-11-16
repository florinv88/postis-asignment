package com.fnkcode.postis.controller;

import com.fnkcode.postis.annotation.ManagerEndpoint;
import com.fnkcode.postis.context.UserContext;
import com.fnkcode.postis.dto.ErrorRequestDTO;
import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.records.NewVacationRequest;
import com.fnkcode.postis.records.RequestDecision;
import com.fnkcode.postis.records.User;
import com.fnkcode.postis.service.VacationRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name="VACATION REQUEST API",
        description = "Vacation requests can be made by employees via an HTTP API, and managers can receive a summary of all requests and decide whether to accept or reject them."
)
@RestController
@RequestMapping(value = "/vacation", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;
    private final UserContext userContext;

    @Operation(
            summary = "Get personal requests",
            description = "REST API to retrieve individual requests based on the authenticated user, sorted by status (accepted, pending, or denied)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS OK",
            content = @Content(
                    schema = @Schema(implementation = RequestResponseDTO.class)
            )

    )
    @GetMapping(value = "requests")
    public ResponseEntity<RequestResponseDTO> getPersonalRequests(@RequestParam
                                                                  @Pattern(regexp = "approved|pending|rejected", message = "The status of the request it's invalid!")
                                                                  String status) {
        User user = userContext.getUser();
        RequestResponseDTO response = vacationRequestService.getAllRequestsBasedOnAuthor(user.id(), status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @Operation(
            summary = "See the number of remaining vacation days",
            description = "REST API to see the number of remaining vacation days based on the authenticated user."
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS OK"
    )
    @GetMapping(value = "days")
    public ResponseEntity<RequestResponseDTO> getNumberOfRemainingDays() {
        User user = userContext.getUser();
        RequestResponseDTO response = vacationRequestService.getNumberOfRemainingDays(user.id());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(
            summary = "Create a vacation request",
            description = "REST API create a vacation request based on the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "406",
                    description = "Not allowed",
                    content = @Content(
                            schema = @Schema(implementation = ErrorRequestDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorRequestDTO.class)
                    )
            )
    }
    )
    @PostMapping(value = "requests")
    public ResponseEntity<RequestResponseDTO> createVacationRequest(@Valid @RequestBody NewVacationRequest vacationRequest) {
        User user = userContext.getUser();
        RequestResponseDTO response = vacationRequestService.createVacationRequest(user.id(), vacationRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @Operation(
            summary = "MANAGERS ONLY : Get all requests",
            description = "REST API to get all vacation requests filtered by the status."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "20",
                    description = "HTTP Status OK"
            )
    }
    )
    @ManagerEndpoint
    @GetMapping(value = "staff/requests")
    public ResponseEntity<RequestResponseDTO> getAllEmployeesRequests(@RequestParam
                                                                      @Pattern(regexp = "pending|approved",message = "The status of the request it's not allowed")
                                                                      String status) {
        RequestResponseDTO response = vacationRequestService.getAllRequestsBasedOn(status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(
            summary = "MANAGERS ONLY : Get all requests from a user",
            description = "REST API to get all vacation requests made by a specific user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "20",
                    description = "HTTP Status OK"
            )
    }
    )
    @ManagerEndpoint
    @GetMapping(value = "staff/requests/{id}")
    public ResponseEntity<RequestResponseDTO> getEmployeeRequests(@PathVariable
                                                                  @NotNull(message = "The id it's mandatory.")
                                                                  @Pattern(regexp = "^[0-9]+$",message = "The id is invalid!")
                                                                  String id) {
        RequestResponseDTO response = vacationRequestService.getAllRequestsBasedOnAuthor(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @Operation(
            summary = "MANAGERS ONLY : make a decision",
            description = "REST API make a decision about a vacation request."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "20",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorRequestDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorRequestDTO.class)
                    )
            )
    }
    )
    @ManagerEndpoint
    @PutMapping(value = "staff/requests")
    public ResponseEntity<RequestResponseDTO> makeDecision(@Valid @RequestBody RequestDecision requestDecision) {
        User user = userContext.getUser();
        RequestResponseDTO response = vacationRequestService.makeDecision(requestDecision, user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);


    }


    @Operation(
            summary = "MANAGERS ONLY : Get all overlapping requests",
            description = "REST API to get all all overlapping requests"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "20",
                    description = "HTTP Status OK"
            )
    }
    )
    @ManagerEndpoint
    @GetMapping(value = "staff/requests/overlapping")
    public ResponseEntity<RequestResponseDTO> getOverlappingRequests() {
        RequestResponseDTO response = vacationRequestService.getAllOverlappingRequests();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
