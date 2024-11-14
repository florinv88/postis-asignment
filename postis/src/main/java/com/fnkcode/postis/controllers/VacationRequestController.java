package com.fnkcode.postis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fnkcode.postis.dto.RequestResponseDTO;
import com.fnkcode.postis.records.User;
import com.fnkcode.postis.services.VacationRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.fnkcode.postis.utils.JwtUtils.extractUserFromJwtToken;

@RestController
@RequestMapping("/vacation")
@Slf4j
@RequiredArgsConstructor
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "demo")
    public String demo(){
        String jwtToken = request.getHeader("Authorization");
        User user;
        try {
             user = extractUserFromJwtToken(jwtToken);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("demo controller api called");
        return "this is a demo";
    }


    @GetMapping(value = "requests")
    public ResponseEntity<RequestResponseDTO> getPersonalRequests(@RequestParam String status){
        // statusului o sa ii fie inpus prin pattern unul din cele 3


        User user = this.getUser();
        RequestResponseDTO response = vacationRequestService.getAllRequestsBasedOn(user.id(), status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    private User getUser(){
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
