package com.hotel.management.hotel_management.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
/**
 * This component handles unauthorized access to secured resources. It implements the
 * AuthenticationEntryPoint interface to return a 401 Unauthorized response whenever an
 * authentication exception occurs, indicating that access to the requested resource
 * requires authentication.
 */
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
    }
}
