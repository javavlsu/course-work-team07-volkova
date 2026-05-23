package com.example.prs.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.prs.dto.RegisterDto;
import com.example.prs.model.Order;
import com.example.prs.model.User;
import com.example.prs.model.enums.UserRole;
import com.example.prs.repository.OrderRepository;
import com.example.prs.repository.UserRepository;
import com.example.prs.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
    }

    public void registerUser(String firstName,
                         String lastName,
                         String login,
                         String email,
                         String number,
                         UserRole role,
                         String password) {

        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Этот логин уже зарегистрирован");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Этот email уже зарегистрирован");
        }

        if (userRepository.existsByNumber(number)) {
            throw new IllegalArgumentException("Этот номер уже зарегистрирован");
        }

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setEmail(email);
        user.setNumber(number);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }


    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {

            throw new IllegalStateException("Пользователь не найден или не авторизован");
        }

        return userDetails.getUser();
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found: " + login));
    }

    public void updateCurrentUser(String firstName,
                                String lastName,
                                String login,
                                String email,
                                String number) {

        User user = getCurrentUser();

        if (userRepository.existsByLoginAndIdNot(login, user.getId())) {
            throw new IllegalArgumentException("Логин уже используется");
        }

        if (userRepository.existsByEmailAndIdNot(email, user.getId())) {
            throw new IllegalArgumentException("Email уже используется");
        }

        if (userRepository.existsByNumberAndIdNot(number, user.getId())) {
            throw new IllegalArgumentException("Телефон уже используется");
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setLogin(login);
        user.setEmail(email);
        user.setNumber(number);

        userRepository.save(user);
    }

    public void updatePassword(User user, String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    public Map<UserRole, Long> getUserRoleCount() {

        return userRepository.countUsersByRole()
                .stream()
                .collect(Collectors.toMap(
                        row -> (UserRole) row[0],
                        row -> (Long) row[1] 
                ));
    }

    public void deleteUser(Long id, String adminPassword) {

        User admin = getCurrentUser();

        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            throw new IllegalStateException("Неверный пароль администратора");
        }

        User user = userRepository.findById(id).orElseThrow();

        List<Order> orders = orderRepository.findByClientId(user.getId());

        for (Order order : orders) {
            order.setClient(null);
        }

        orderRepository.saveAll(orders);
        userRepository.delete(user);
    }

    public void updateUser(Long id,
                       String login,
                       String email,
                       String firstName,
                       String lastName,
                       String number,
                       UserRole role,
                       String newPassword,
                       String confirmPassword, 
                       String adminPassword) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        User admin = getCurrentUser();

        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            throw new IllegalStateException("Неверный пароль администратора");
        }

        if (login == null || login.isBlank())
            throw new IllegalStateException("Логин обязателен");

        if (firstName == null || firstName.isBlank())
            throw new IllegalStateException("Имя обязательно");

        if (lastName == null || lastName.isBlank())
            throw new IllegalStateException("Фамилия обязательна");

        if (email == null || email.isBlank())
            throw new IllegalStateException("Email обязателен");

        if (!email.contains("@"))
            throw new IllegalStateException("Некорректный email");

        if (number == null || number.isBlank())
            throw new IllegalStateException("Телефон обязателен");

        if (!number.matches("^[0-9+\\-() ]+$"))
            throw new IllegalStateException("Некорректный номер телефона");

        if (role == null)
            throw new IllegalStateException("Роль обязательна");

        user.setLogin(login);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setNumber(number);
        user.setRole(role);

        if (newPassword != null && !newPassword.isBlank()) {

            if (newPassword.length() < 6)
                throw new IllegalStateException("Новый пароль должен быть минимум 6 символов");

            if (!newPassword.equals(confirmPassword))
                throw new IllegalStateException("Пароли не совпадают");

            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
    }

    public void createUserByAdmin(RegisterDto dto, String adminPassword) {

        User admin = getCurrentUser();

        if (!passwordEncoder.matches(adminPassword, admin.getPassword())) {
            throw new IllegalStateException("Неверный пароль администратора");
        }

        if (userRepository.existsByLogin(dto.getLogin())) {
            throw new IllegalArgumentException("Логин уже используется");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }

        if (userRepository.existsByNumber(dto.getNumber())) {
            throw new IllegalArgumentException("Телефон уже используется");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setNumber(dto.getNumber());
        user.setRole(dto.getRole());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
    }

    public Page<User> getUsers(UserRole role, Pageable pageable) {

        if (role == null) {
            return userRepository.findAll(pageable);
        }

        return userRepository.findByRole(role, pageable);
    }
}