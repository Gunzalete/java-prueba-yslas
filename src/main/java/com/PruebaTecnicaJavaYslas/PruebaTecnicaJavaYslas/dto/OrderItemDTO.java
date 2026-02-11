package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;

    @NotNull(message = "El ID del producto es requerido")
    private Long productId;

    private String productName;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer quantity;

    private BigDecimal unitPrice;
}