package com.example.prs.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.prs.model.User;
import com.example.prs.model.enums.UserRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
    Optional<User> findByNumber(String number);

    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByNumber(String number);

    boolean existsByLoginAndIdNot(String login, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByNumberAndIdNot(String number, Long id);

    @Query("select u.role, count(u) from User u group by u.role")
    List<Object[]> countUsersByRole();

    Page<User> findByRole(UserRole role, Pageable pageable);
}
