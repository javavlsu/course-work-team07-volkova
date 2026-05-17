package com.example.prs.controller;

import com.example.prs.dto.RegisterDto;
import com.example.prs.model.enums.UserRole;
import com.example.prs.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

@PostMapping("/register")
public String register(@Valid @ModelAttribute RegisterDto dto, 
                       BindingResult result, 
                       HttpServletRequest request) {
    
    if (!dto.getPassword().equals(dto.getConfirmPassword())) { 
        result.rejectValue("confirmPassword", "", "Пароли не совпадают"); 
    } 
    
    if (result.hasErrors()) { 
        return "auth/register"; 
    } 
    
    try { 
        userService.registerUser( 
            dto.getFirstName(), 
            dto.getLastName(), 
            dto.getLogin(), 
            dto.getEmail(), 
            dto.getNumber(), 
            UserRole.CLIENT,
            dto.getPassword()
        ); 
    } catch (IllegalArgumentException e) { 
        result.reject("", e.getMessage()); 
        return "auth/register"; 
    } 

    Authentication auth = authenticationManager.authenticate( 
        new UsernamePasswordAuthenticationToken( 
            dto.getLogin(), 
            dto.getPassword() 
        ) 
    ); 
    
    SecurityContextHolder.getContext().setAuthentication(auth); 
    request.getSession().setAttribute( 
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
        SecurityContextHolder.getContext() 
    ); 
    
    return "redirect:/"; 
}

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}