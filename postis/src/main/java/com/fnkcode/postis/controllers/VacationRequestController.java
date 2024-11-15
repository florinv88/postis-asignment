package com.fnkcode.postis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.records.NewVacationRequest;
import com.fnkcode.postis.records.RequestDecision;
import com.fnkcode.postis.records.User;
import com.fnkcode.postis.services.VacationRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.fnkcode.postis.constants.MessageConstants.FAILED;
import static com.fnkcode.postis.constants.MessageConstants.SUCCESS;
import static com.fnkcode.postis.utils.JwtUtils.extractUserFromJwtToken;

@RestController
@RequestMapping(value = "/vacation", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;
    private final HttpServletRequest request;


    @GetMapping(value = "requests")
    public ResponseEntity<RequestResponseDTO> getPersonalRequests(@RequestParam String status) {
        // statusului o sa ii fie inpus prin pattern unul din cele 3

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
    public ResponseEntity<RequestResponseDTO> createVacationRequest(@RequestBody NewVacationRequest vacationRequest) {
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
