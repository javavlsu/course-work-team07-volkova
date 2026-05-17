package com.example.prs.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        var roles = authentication.getAuthorities();

        // ADMIN
        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/admin/managepanel");
            return;
        }

        // EMPLOYEE
        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_EMPLOYEE"))) {
            response.sendRedirect("/employee/dashboard");
            return;
        }

        // CLIENT
        if (roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_CLIENT"))) {
            response.sendRedirect("/");
            return;
        }

        // fallback
        response.sendRedirect("/");
    }
}