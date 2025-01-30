package com.api.sysagua.service;

import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {

    void registerProduct(CreateProductDto productDto);

    List<Product> getProducts(Long id,
                              String name,
                              BigDecimal priceStart,
                              BigDecimal priceEnd,
                              BigDecimal costStart,
                              BigDecimal costEnd,
                              String category,
                              String unit,
                              String brand,
                              LocalDateTime startUpdateDate,
                              LocalDateTime endUpdateDate,
                              LocalDateTime startRegisterDate,
                              LocalDateTime endRegisterDate,
                              Boolean active,
                              String line,
                              String ncm);

    void updateProduct(Long id, UpdateProductDto dto);

    void delete(Long id);
}
