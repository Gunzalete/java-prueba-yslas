package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.ProductDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.repository.ProductRepository;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_rejectsBlankName() {
        ProductDTO dto = ProductDTO.builder()
                .name(" ")
                .description("desc")
                .price(new BigDecimal("10.00"))
                .build();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> productService.createProduct(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }
}
