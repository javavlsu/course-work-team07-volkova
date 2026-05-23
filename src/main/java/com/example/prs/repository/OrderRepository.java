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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClientId(Long clientId);

    List<Order> findByPhoneBrandId(Long phoneBrandId);

    @Modifying
    @Query("update Order o set o.employee.id = :employeeId where o.id = :orderId")
    int setEmployee(@Param("orderId") Long orderId, @Param("employeeId") Long employeeId);

    @Query("select o.status, count(o) from Order o group by o.status")
    List<Object[]> countOrdersByStatus();

    Page<Order> findByClient(User client, Pageable pageable);
    Page<Order> findByClientAndStatus(User client, OrderStatus status, Pageable pageable);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByEmployeeId(Long employeeId, Pageable pageable);
    Page<Order> findByEmployeeIdAndStatus(Long employeeId, OrderStatus status, Pageable pageable);
}