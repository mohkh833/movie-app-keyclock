package com.project.movies.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ExceptionResponse body = ExceptionResponse.builder()
                .businessErrorCode(FORBIDDEN.value())
                .businessExceptionDescription("You do not have permission to perform this action.")
                .error(accessDeniedException.getMessage())
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

}
