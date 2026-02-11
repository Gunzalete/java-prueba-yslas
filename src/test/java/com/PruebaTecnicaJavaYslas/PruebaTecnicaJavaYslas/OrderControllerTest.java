package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.controller.OrderController;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderItemDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service.IOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IOrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_returnsOrderWithCalculatedTotal() throws Exception {
        OrderItemDTO itemDto = new OrderItemDTO(null, 1L, "Mouse", 2, new BigDecimal("10.00"));
        OrderDTO requestDto = OrderDTO.builder()
                .orderNumber(1001)
                .orderDate(LocalDate.now())
                .items(List.of(itemDto))
                .build();

        OrderDTO responseDto = OrderDTO.builder()
                .id(1L)
                .orderNumber(1001)
                .orderDate(LocalDate.now())
                .total(new BigDecimal("20.00"))
                .items(List.of(itemDto))
                .build();

        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.total").value(20.00))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    void getOrderById_returnsOrderDetails() throws Exception {
        OrderItemDTO itemDto = new OrderItemDTO(1L, 1L, "Mouse", 2, new BigDecimal("10.00"));
        OrderDTO orderDto = OrderDTO.builder()
                .id(1L)
                .orderNumber(1001)
                .orderDate(LocalDate.now())
                .total(new BigDecimal("20.00"))
                .items(List.of(itemDto))
                .build();

        when(orderService.getOrder(1L)).thenReturn(orderDto);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.orderNumber").value(1001))
                .andExpect(jsonPath("$.total").value(20.00));
    }


}
