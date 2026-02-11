package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface IProductService {
    Page<ProductDTO> listProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    ProductDTO getProduct(Long id);
    ProductDTO createProduct(ProductDTO productDto);
    ProductDTO updateProduct(Long id, ProductDTO productDto);
    void deleteProduct(Long id);
}