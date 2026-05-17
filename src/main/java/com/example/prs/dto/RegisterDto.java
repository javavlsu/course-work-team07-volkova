package com.example.prs.dto;

import com.example.prs.model.enums.UserRole;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Поле обязательно для заполнения")
    private String firstName;

    @NotBlank(message = "Поле обязательно для заполнения")
    private String lastName;

    @NotBlank(message = "Поле обязательно для заполнения")
    private String login;

    @NotBlank(message = "Поле обязательно для заполнения")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Поле обязательно для заполнения")
    @Pattern(regexp = "^[0-9+\\-() ]+$", message = "Некорректный номер телефона")
    private String number;

    @NotNull(message = "Роль обязательна")
    private UserRole role = UserRole.CLIENT; ;

    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    private String confirmPassword;
}