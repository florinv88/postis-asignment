package com.fnkcode.postis.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fnkcode.postis.context.UserContext;
import com.fnkcode.postis.exception.PermisionDeniedException;
import com.fnkcode.postis.records.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.fnkcode.postis.enums.Roles.MANAGER;
import static com.fnkcode.postis.utils.JwtUtils.extractUserFromJwtToken;

@Aspect
@Component
@RequiredArgsConstructor
public class RequestInterceptor {
    private final HttpServletRequest request;
    private final UserContext userContext;

    @Before("target(com.fnkcode.postis.controller.VacationRequestController)")
    public void interceptAllRequests(){
        userContext.setUser(getUser());
    }

    @Before("@annotation(com.fnkcode.postis.annotation.ManagerEndpoint)")
    public void interceptManagerRequest(){
        User user = userContext.getUser();
        if (!user.role().equals(MANAGER.getName())){
            throw new PermisionDeniedException("You don't have permision to access this endpoint.");
        }
        System.out.println("Request intercepted");
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
