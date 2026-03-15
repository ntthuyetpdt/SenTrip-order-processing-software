package com.example.da_sentrip.security;

import com.example.da_sentrip.utils.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String message = (ex.getCause() instanceof ExpiredJwtException)
                ? "Token has expired" : "Unauthorized";

        mapper.writeValue(response.getWriter(), Map.of(
                "status", Constants.HTTP_STATUS.UNAUTHORIZED,
                "message", message
        ));
    }
}