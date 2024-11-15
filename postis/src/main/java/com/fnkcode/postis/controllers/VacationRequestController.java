package com.fnkcode.postis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.records.NewVacationRequest;
import com.fnkcode.postis.records.RequestDecision;
import com.fnkcode.postis.records.User;
import com.fnkcode.postis.services.VacationRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fnkcode.postis.constants.MessageConstants.FAILED;
import static com.fnkcode.postis.constants.MessageConstants.SUCCESS;
import static com.fnkcode.postis.utils.JwtUtils.extractUserFromJwtToken;

@RestController
@RequestMapping(value = "/vacation", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;
    private final HttpServletRequest request;


    @GetMapping(value = "requests")
    public ResponseEntity<RequestResponseDTO> getPersonalRequests(@RequestParam
                                                                  @Pattern(regexp = "approved|pending|rejected", message = "The status of the request it's invalid!")
                                                                  String status) {
        User user = this.getUser();
        RequestResponseDTO response = vacationRequestService.getAllRequestsBasedOn(user.id(), status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @GetMapping(value = "days")
    public ResponseEntity<RequestResponseDTO> getNumberOfRemainingDays() {
        User user = this.getUser();
        RequestResponseDTO response = vacationRequestService.getNumberOfRemainingDays(user.id());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping(value = "requests")
    public ResponseEntity<RequestResponseDTO> createVacationRequest(@Valid @RequestBody NewVacationRequest vacationRequest) {
        //trebuie adaugata validare pe pattern dates si obligativitate

        User user = this.getUser();
        RequestResponseDTO response = vacationRequestService.createVacationRequest(user.id(), vacationRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @GetMapping(value = "staff/requests")
    public ResponseEntity<RequestResponseDTO> getAllEmployeesRequests(@RequestParam String status) {
        // statusului o sa ii fie inpus prin pattern unul din cele 2!!!!

        RequestResponseDTO response = vacationRequestService.getAllRequestsBasedOn(status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(value = "staff/requests/{id}")
    public ResponseEntity<RequestResponseDTO> getEmployeeRequests(@PathVariable long id) {
        RequestResponseDTO response = vacationRequestService.getAllRequestsBasedOn(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping(value = "staff/requests")
    public ResponseEntity<RequestResponseDTO> makeDecision(@RequestBody RequestDecision requestDecision) {
        //requestDecision --> decision sa aiba patern
        User user = this.getUser();
        RequestResponseDTO response = vacationRequestService.isUpdated(requestDecision, user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);


    }

    @GetMapping(value = "staff/requests/overlapping")
    public ResponseEntity<RequestResponseDTO> getOverlappingRequests() {
        RequestResponseDTO response = vacationRequestService.getAllOverlappingRequests();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    private User getUser() {
        String jwtToken = request.getHeader("Authorization");
        User user;
        try {
            user = extractUserFromJwtToken(jwtToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}
