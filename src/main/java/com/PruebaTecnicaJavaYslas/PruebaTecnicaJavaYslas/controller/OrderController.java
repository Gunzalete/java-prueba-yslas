package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.controller;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service.IOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService service;

    @GetMapping
    public Page<OrderDTO> list(
            @RequestParam(required = false) Integer orderNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return service.listOrders(orderNumber, fromDate, toDate, pageable);
    }

    @GetMapping("/{id}")
    public OrderDTO getById(@PathVariable Long id) {
        return service.getOrder(id);
    }

    @PostMapping
    public OrderDTO create(@Valid @RequestBody OrderDTO dto) {
        return service.createOrder(dto);
    }

    @PutMapping("/{id}")
    public OrderDTO update(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
        return service.updateOrder(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteOrder(id);
    }
}
