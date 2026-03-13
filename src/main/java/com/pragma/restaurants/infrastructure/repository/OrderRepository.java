package com.pragma.restaurants.infrastructure.repository;

import com.pragma.restaurants.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT COUNT(o) > 0 FROM OrderEntity o WHERE o.clientId = :clientId " +
            "AND o.status IN ('PENDING', 'IN_PREPARATION', 'READY')")
    boolean existsActiveOrderByClientId(@Param("clientId") Long clientId);
}
