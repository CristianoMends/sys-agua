package com.api.sysagua.service.impl;

import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.SearchProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Product;
import com.api.sysagua.repository.ProductRepository;
import com.api.sysagua.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void registerProduct(CreateProductDto productDto) {
        var productToSave = productDto.toModel();
        productToSave.setRegisteredAt(LocalDate.now(ZoneOffset.of("-03:00")));
        productToSave.setUpdatedAt(LocalDate.now(ZoneOffset.of("-03:00")));

        this.productRepository.save(productToSave);
    }

    @Override
    public List<Product> getProducts(SearchProductDto p) {
        if (p.getName()     == null ) p.setName("");
        if (p.getCategory() == null ) p.setCategory("");
        if (p.getUnit()     == null ) p.setUnit("");
        if (p.getBrand()    == null ) p.setBrand("");


        if (p.getStartUpdateDate() != null && p.getEndRegisterDate() != null
                && p.getStartUpdateDate().isAfter(p.getEndRegisterDate())) {
            throw new BusinessException("Start date cannot be after end date.", HttpStatus.BAD_REQUEST);
        }

        return this.productRepository.findByFilters(
                p.getId(),
                p.getName(),
                p.getCategory(),
                p.getUnit(),
                p.getBrand(),
                p.getStartUpdateDate(),
                p.getEndUpdateDate(),
                p.getStartRegisterDate(),
                p.getEndRegisterDate()
        );
    }

    @Override
    public void updateProduct(Long id, UpdateProductDto dto) {
        Product p = this.productRepository.findById(id).orElseThrow(()->
                new BusinessException("Product not found",HttpStatus.NOT_FOUND));

        if (dto.getBrand()      != null) p.setBrand(    dto.getBrand());
        if (dto.getCategory()   != null) p.setCategory( dto.getCategory());
        if (dto.getUnit()       != null) p.setUnit(     dto.getUnit());
        if (dto.getName()       != null) p.setName(     dto.getName());

        p.setUpdatedAt(LocalDate.now(ZoneOffset.of("-03:00")));
        this.productRepository.save(p);
    }
}
