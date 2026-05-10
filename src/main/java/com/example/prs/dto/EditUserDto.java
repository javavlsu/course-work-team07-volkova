package com.example.prs.dto;

import com.example.prs.model.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class EditUserDto {

    @NotBlank(message = "Логин обязателен")
    private String login;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^[0-9+\\-() ]+$", message = "Некорректный номер телефона")
    private String number;

    @NotNull(message = "Роль обязательна")
    private UserRole role;

    @NotBlank(message = "Введите старый пароль")
    private String oldPassword;

    @Size(min = 6, message = "Новый пароль должен быть минимум 6 символов")
    private String newPassword;

    private String confirmPassword;
}