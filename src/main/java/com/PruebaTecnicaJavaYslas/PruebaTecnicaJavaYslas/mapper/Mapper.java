package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.mapper;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderItemDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.ProductDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Order;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.OrderItem;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Product;

import java.util.List;

public class Mapper {
    public static ProductDTO toDTO(Product p) {
        if (p == null) return null;

        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .build();
    }

    public static OrderDTO toDTO(Order o) {
        if (o == null) return null;

        return OrderDTO.builder()
                .id(o.getId())
                .orderNumber(o.getOrderNumber())
                .orderDate(o.getOrderDate())
                .total(o.getTotal())
                .items(toItemDTOs(o.getItems()))
                .build();
    }

    public static OrderItemDTO toDTO(OrderItem item) {
        if (item == null) return null;

        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
        dto.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        return dto;
    }

    public static List<OrderItemDTO> toItemDTOs(List<OrderItem> items) {
        if (items == null) return List.of();
        return items.stream().map(Mapper::toDTO).toList();
    }
}