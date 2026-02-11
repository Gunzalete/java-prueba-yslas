package com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.service;

import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.dto.ProductDTO;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.mapper.Mapper;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.model.Product;
import com.PruebaTecnicaJavaYslas.PruebaTecnicaJavaYslas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService implements IProductService{

    @Autowired
    private ProductRepository repo;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> listProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return repo.search(name, minPrice, maxPrice, pageable).map(Mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProduct(Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return Mapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDto) {
        validateProduct(productDto);
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        return Mapper.toDTO(repo.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDto) {
        validateProduct(productDto);
        Product product = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        return Mapper.toDTO(repo.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        repo.deleteById(id);
    }

    private void validateProduct(ProductDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto es requerido");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser mayor a 0");
        }
    }
}