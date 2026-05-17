package com.example.prs.controller;

import com.example.prs.model.User;
import com.example.prs.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/client")
public class ProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Model model) {

        model.addAttribute("user", userService.getCurrentUser());

        return "client/profile";
    }

    @GetMapping("/editprofile")
    public String editProfile(Model model) {
        
        model.addAttribute("user", userService.getCurrentUser());
        
        return "client/editprofile";
    }

    @PostMapping("/editprofile")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String login,
                                @RequestParam String email,
                                @RequestParam String number,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        try {
            userService.updateCurrentUser(firstName, lastName, login, email, number);

            redirectAttributes.addFlashAttribute("successMessage", "Профиль успешно обновлён!");
            return "redirect:/client/profile";

        } catch (IllegalArgumentException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("user", userService.getCurrentUser());

            return "client/editprofile";
        }
    }

    @GetMapping("/changepassword")
    public String changePassword(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "client/changepassword";
    }

    @PostMapping("/changepassword")
    public String updatePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("errorOldPassword", "Старый пароль неверный");
            return "client/changepassword";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("errorConfirmPassword", "Пароли не совпадают");
            return "client/changepassword";
        }

        if (newPassword.length() < 6) {
            model.addAttribute("errorNewPassword", "Пароль должен быть минимум 6 символов");
            return "client/changepassword";
        }

        userService.updatePassword(user, newPassword);

        redirectAttributes.addFlashAttribute("successMessage", "Пароль успешно изменён!");
        return "redirect:/client/profile";
    }
    
}
