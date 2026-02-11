package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.OrderItem;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;

    @NotNull(message = "El número de orden es requerido")
    @Positive(message = "El número de orden debe ser positivo")
    private Integer orderNumber;

    private LocalDate orderDate;
    private BigDecimal total;

    @NotNull(message = "La orden debe tener al menos un item")
    @Size(min = 1, message = "La orden debe tener al menos un item")
    @Valid
    private List<OrderItemDTO> items;
}