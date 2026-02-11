package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.repository;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            select o from Order o
            where (:orderNumber is null or o.orderNumber = :orderNumber)
              and (:fromDate is null or o.orderDate >= :fromDate)
              and (:toDate is null or o.orderDate <= :toDate)
            """)
    Page<Order> search(
            @Param("orderNumber") Integer orderNumber,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );
}