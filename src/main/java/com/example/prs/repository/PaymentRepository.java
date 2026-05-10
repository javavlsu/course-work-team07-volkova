package com.example.prs.repository;

import com.example.prs.model.Payment;
import com.example.prs.model.Order;
import com.example.prs.model.enums.PaymentMethod;
import com.example.prs.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления платежами по заказам
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Поиск платежа по связанному заказу
     * @param orderId id заказа
     * @return Optional с найденным платежом
     */
    Optional<Payment> findByOrderId(Long orderId);

    /**
     * Поиск платежей с указанным статусом
     * @param status статус платежа
     * @return список платежей
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Поиск платежей по указанному методом оплаты
     * @param method метод оплаты
     * @return список платежей
     */
    List<Payment> findByMethod(PaymentMethod method);

    /**
     * Поиск всех платежей за указанный период
     * @param startDate начало периода
     * @param endDate конец периода
     * @return список платежей
     */
    List<Payment> findByPaidAt(LocalDateTime startDate, LocalDateTime endDate);

}