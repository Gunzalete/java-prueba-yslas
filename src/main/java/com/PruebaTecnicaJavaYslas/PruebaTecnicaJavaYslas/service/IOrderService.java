package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IOrderService {
    Page<OrderDTO> listOrders(Integer orderNumber, LocalDate fromDate, LocalDate toDate, Pageable pageable);
    OrderDTO getOrder(Long id);
    OrderDTO createOrder(OrderDTO orderDto);
    OrderDTO updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);
}