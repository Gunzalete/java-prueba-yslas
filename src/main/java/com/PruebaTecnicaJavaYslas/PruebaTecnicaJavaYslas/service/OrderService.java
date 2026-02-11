package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.OrderItemDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.mapper.Mapper;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Order;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.OrderItem;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Product;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.repository.OrderRepository;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> listOrders(Integer orderNumber, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return orderRepo.search(orderNumber, fromDate, toDate, pageable).map(Mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));
        return Mapper.toDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDto) {
        Order order = buildOrder(orderDto, null);
        return Mapper.toDTO(orderRepo.save(order));
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existing = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));
        Order order = buildOrder(orderDTO, existing);
        return Mapper.toDTO(orderRepo.save(order));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada");
        }
        orderRepo.deleteById(id);
    }

    private Order buildOrder(OrderDTO dto, Order target) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La orden debe tener al menos un item");
        }

        LocalDate orderDate = dto.getOrderDate() != null ? dto.getOrderDate() : LocalDate.now();
        if (orderDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de la orden no puede ser futura");
        }

        Order order = target != null ? target : new Order();
        order.setOrderNumber(dto.getOrderNumber());
        order.setOrderDate(orderDate);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO itemDto : dto.getItems()) {
            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad del item debe ser positiva");
            }
            Product product = productRepo.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setOrder(order);
            items.add(item);

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            total = total.add(lineTotal);
        }

        order.getItems().clear();
        order.getItems().addAll(items);
        order.setTotal(total);
        return order;
    }
}