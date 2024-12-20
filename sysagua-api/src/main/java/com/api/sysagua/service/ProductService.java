package com.api.sysagua.service;

import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.SearchProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.model.Product;

import java.util.List;

public interface ProductService {

    void registerProduct(CreateProductDto productDto);

    List<Product> getProducts(SearchProductDto productDto);

    void updateProduct(Long id, UpdateProductDto dto);
}
