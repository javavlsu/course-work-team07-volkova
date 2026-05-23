package com.example.prs.controller;

import com.example.prs.dto.RegisterDto;
import com.example.prs.model.User;
import com.example.prs.model.enums.OrderStatus;
import com.example.prs.model.enums.UserRole;
import com.example.prs.security.CustomUserDetails;
import com.example.prs.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/managepanel")
    public String managePanel(Model model) {
        Map<UserRole, Long> count = userService.getUserRoleCount();

        model.addAttribute("totalUsers", count.values().stream().mapToLong(Long::longValue).sum());
        model.addAttribute("clients", count.getOrDefault(UserRole.CLIENT, 0L));
        model.addAttribute("employees", count.getOrDefault(UserRole.EMPLOYEE, 0L));
        model.addAttribute("admins", count.getOrDefault(UserRole.ADMIN, 0L));

        return "admin/managepanel";
    }

   @GetMapping("/users")
    public String users(
        @RequestParam(defaultValue = "ALL") String role,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model) {

        User currentUser = userService.getCurrentUser();

        Sort sort = sortDir.equals("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        UserRole userRole = null;

        if (!"ALL".equals(role)) {

            try {
                userRole = UserRole.valueOf(role);
            } catch (Exception ignored) {}
        }

        Page<User> usersPage = userService.getUsers(userRole, pageable);

        List<UserRole> roles = Arrays.stream(UserRole.values()).sorted(Comparator.reverseOrder()).toList();

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("usersPage", usersPage);
        model.addAttribute("currentRole", role);
        model.addAttribute("currentSort", sortBy);
        model.addAttribute("currentDir", sortDir);
        model.addAttribute("roles", roles);

        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, @RequestParam String adminPassword, RedirectAttributes ra) {
        try {
            userService.deleteUser(id, adminPassword);
            ra.addFlashAttribute("successMessage", "Пользователь успешно удален!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/users";
    }

   @PostMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id,
                        @RequestParam String login,
                        @RequestParam String email,
                        @RequestParam String firstName,
                        @RequestParam String lastName,
                        @RequestParam String number,
                        @RequestParam UserRole role,
                        @RequestParam(required = false) String newPassword,
                        @RequestParam(required = false) String confirmPassword,
                        @RequestParam String adminPassword,
                        RedirectAttributes ra) {

        try {
            userService.updateUser(id, login, email, firstName, lastName, number, role, newPassword, confirmPassword, adminPassword);

            ra.addFlashAttribute("successMessage", "Данные пользователя успешно обновлены!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/users/create")
    public String createUser(@Valid @ModelAttribute RegisterDto dto,
                            BindingResult result,
                            @RequestParam String adminPassword,
                            RedirectAttributes ra) {

        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage",
                    result.getFieldErrors()
                            .stream()
                            .map(e -> e.getDefaultMessage())
                            .findFirst()
                            .orElse("Ошибка валидации"));

            return "redirect:/admin/users";
        }

        try {
            userService.createUserByAdmin(dto, adminPassword);
            ra.addFlashAttribute("successMessage", "Пользователь успешно создан!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/users";
    }
}