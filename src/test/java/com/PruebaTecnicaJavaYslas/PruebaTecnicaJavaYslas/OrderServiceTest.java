package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderItemDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Order;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Product;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.repository.OrderRepository;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.repository.ProductRepository;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_calculatesTotalAndSetsItemPrice() {
        Product product = new Product(1L, "Mouse", "Gamer", new BigDecimal("10.00"));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderItemDTO item = new OrderItemDTO(null, 1L, null, 2, null);
        OrderDTO dto = OrderDTO.builder()
                .orderNumber(1001)
                .orderDate(LocalDate.now())
                .items(List.of(item))
                .build();

        OrderDTO result = orderService.createOrder(dto);

        assertEquals(new BigDecimal("20.00"), result.getTotal());
        assertEquals(1, result.getItems().size());
        assertEquals(new BigDecimal("10.00"), result.getItems().get(0).getUnitPrice());
    }

    @Test
    void createOrder_rejectsFutureDate() {
        OrderItemDTO item = new OrderItemDTO(null, 1L, null, 1, null);
        OrderDTO dto = OrderDTO.builder()
                .orderNumber(1002)
                .orderDate(LocalDate.now().plusDays(1))
                .items(List.of(item))
                .build();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> orderService.createOrder(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }
}
