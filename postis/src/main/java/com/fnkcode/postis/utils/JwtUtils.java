package com.fnkcode.postis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnkcode.postis.records.User;


import java.util.Base64;

public final class JwtUtils {

    private JwtUtils(){}

    public static User extractUserFromJwtToken(String jwt) throws JsonProcessingException {
        String[] tokenParts = jwt.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(tokenParts[1]));

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(payload, User.class);
    }
}
