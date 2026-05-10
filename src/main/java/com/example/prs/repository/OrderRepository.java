package com.example.prs.repository;

import com.example.prs.model.Order;
import com.example.prs.model.PhoneBrand;
import com.example.prs.model.User;
import com.example.prs.model.PhoneModel;
import com.example.prs.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления заказами на ремонт
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Поиск всех заказов конкретного клиента
     * @param clientId id клиента
     * @return список заказов клиента
     */
    List<Order> findByClientId(Long clientId);

    /**
     * Поиск всех заказов, назначенных на конкретного сотрудника
     * @param employeeId id сотрудника
     * @return список заказов сотрудника
     */
    List<Order> findByEmployeeId(Long employeeId);
    /**
     * Поиск заказов по статусу
     * @param status статус заказа
     * @return список заказов с указанным статусом
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Поиск заказов, созданных за указанный период
     * @param start начало периода
     * @param end конец периода
     * @return список заказов
     */
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Поиск заказов, ожидающих назначения сотрудника (статус CREATED)
     * @return список новых заказов
     */
    @Query("select o from Order o where o.status = :status and o.employee is null")
    List<Order> findByNullEmployee(@Param("status") OrderStatus status);

    /**
     * Поиск заказов, ожидающих окончания работы (статус CREATED, INREPAIR, READY)
     * @return список новых заказов
     */
    @Query("select o from Order o where (o.status = 'CREATED' and o.employee is null) or (o.status in ('INREPAIR', 'READY') and o.employee is not null)")
    List<Order> findNoCompletedOrCancelled();

    /**
     * Поиск заказов по марке телефона
     * @param phoneBrandId id марки телефона
     * @return список заказов для данной марки
     */
    List<Order> findByPhoneBrandId(Long phoneBrandId);

    /**
     * Поиск заказыов по модели телефона
     * @param phoneModelId id модели телефона
     * @return список заказов для данной модели
     */
    List<Order> findByPhoneModelId(Long phoneModelId);

    /**
     * Обновление статуса заказа
     * @param orderId ID заказа
     * @param newStatus новый статус
     * @return количество обновленных записей
     */
    @Modifying
    @Transactional
    @Query("update Order o set o.status = :newStatus where o.id = :orderId")
    int updateOrderStatus(@Param("orderId") Long orderId, @Param("newStatus") OrderStatus newStatus);

    /**
     * Обновление стоимости заказа
     * @param orderId ID заказа
     * @param newCost новая стоимость
     * @return количество обновленных записей
     */
    @Modifying
    @Transactional
    @Query("update Order o set o.price = :newPrice where o.id = :orderId")
    int updateOrderPrice(@Param("orderId") Long orderId, @Param("newPrice") BigDecimal newPrice);

    /**
     * Назначение сотрудника на заказ
     * @param orderId ID заказа
     * @param employeeId ID сотрудника
     * @return количество обновленных записей
     */
    @Modifying
    @Query("update Order o set o.employee.id = :employeeId where o.id = :orderId")
    int setEmployee(@Param("orderId") Long orderId, @Param("employeeId") Long employeeId);
    /**
     * Подсчет количества заказов по статусам
     * @return статистика по статусам
     */
    @Query("select o.status, count(o) from Order o group by o.status")
    List<Object[]> getStatusesCount();

    @Query("select o from Order o left join fetch o.payment")
    List<Order> findAllWithPayments();

    @Query("select o from Order o left join fetch o.payment where o.id = :id")
    Optional<Order> findByIdWithPayment(@Param("id") Long id);

    @Query("select o from Order o left join fetch o.payment where (o.status = 'CREATED' and o.employee is null) or (o.status in ('INREPAIR', 'READY') and o.employee is not null)")
    List<Order> findNoCompletedOrCancelledwithPayment();

    @Query("""
            select o from Order o
            left join fetch o.phoneBrand
            left join fetch o.phoneModel
            left join fetch o.repairService
            left join fetch o.payment
            where o.client.id = :clientId
            order by o.createdAt desc
            """)
    List<Order> findClientOrdersForPage(@Param("clientId") Long clientId);

    List<Order> findAllByClientOrderByCreatedAtDesc(User client);

    //long countByStatus(OrderStatus status);

    @Query("select o.status, count(o) from Order o group by o.status")
    List<Object[]> countOrdersByStatus();
}