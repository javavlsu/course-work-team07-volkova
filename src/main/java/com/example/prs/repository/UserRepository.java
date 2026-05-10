package com.example.prs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.prs.model.User;
import com.example.prs.model.enums.UserRole;

/**
 * Репозиторий для доступа к данным пользователей системы
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Поиск пользователя по логину
     * @param login логин пользователя (не может быть null или пустым)
     * @return Optional с найденным пользователем или пустой Optional
     * @throws IllegalArgumentException если login = null
     */
    Optional<User> findByLogin(String login);

    /**
     * Поиск пользователя по email
     * @param email email пользователя (не может быть null или пустым)
     * @return Optional с найденным пользователем или пустой Optional
     * @throws IllegalArgumentException если email = null
     */
    Optional<User> findByEmail(String email);

    /**
     * Поиск пользователя по номеру телефона
     * @param number номер телефона (не может быть null или пустым)
     * @return Optional с найденным пользователем или пустой Optional
     */
    Optional<User> findByNumber(String number);

    /**
     * Проверка существования пользователя с указанным логином
     * @param login логин для проверки
     * @return true - пользователь с таким логином уже существует, иначе false
     */
    boolean existsByLogin(String login);

    /**
     * Проверка существования пользователя с указанным email
     * @param email email для проверки
     * @return true - email уже занят, иначе false
     */
    boolean existsByEmail(String email);

    /**
     * Проверка существования пользователя с указанным номером телефона
     * @param number номер телефона для проверки
     * @return true - номер уже занят, иначе false
     */
    boolean existsByNumber(String number);

    /**
     * Поиск всех пользователей с указанной ролью
     * @param role роль пользователя (ADMIN, EMPLOYEE, CLIENT)
     * @return список пользователей с данной ролью (может быть пустым)
     */
    List<User> findByRole(UserRole role);

    /**
     * Поиск пользователей по имени или фамилии (частичное совпадение)
     * @param keyword часть имени или фамилии
     * @return список найденных пользователей
     */
    @Query("select u from User u where lower(u.firstName) like lower(concat('%', :keyword, '%')) or lower(u.lastName) like lower(concat('%', :keyword, '%'))")
    List<User> findByName(@Param("keyword") String keyword);

    boolean existsByLoginAndIdNot(String login, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByNumberAndIdNot(String number, Long id);

    @Query("select u.role, count(u) from User u group by u.role")
    List<Object[]> countUsersByRole();
    
}
