package com.example.prs.repository;

import com.example.prs.model.Order;
import com.example.prs.model.User;
import com.example.prs.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClientId(Long clientId);

    List<Order> findByEmployeeId(Long employeeId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByPhoneBrandId(Long phoneBrandId);

    @Modifying
    @Query("update Order o set o.employee.id = :employeeId where o.id = :orderId")
    int setEmployee(@Param("orderId") Long orderId, @Param("employeeId") Long employeeId);

    List<Order> findAllByClientOrderByCreatedAtDesc(User client);

    @Query("select o.status, count(o) from Order o group by o.status")
    List<Object[]> countOrdersByStatus();
}