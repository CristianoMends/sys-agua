package com.api.sysagua.service.impl;

import com.api.sysagua.dto.product.CreateProductDto;
import com.api.sysagua.dto.product.SearchProductDto;
import com.api.sysagua.dto.product.UpdateProductDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Product;
import com.api.sysagua.repository.ProductCategoryRepository;
import com.api.sysagua.repository.ProductLineRepository;
import com.api.sysagua.repository.ProductRepository;
import com.api.sysagua.repository.StockRepository;
import com.api.sysagua.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductLineRepository productLineRepository;

    @Override
    public void registerProduct(CreateProductDto dto) {

        var line = this.productLineRepository.findById(dto.getLineId()).orElseThrow(
                () -> new BusinessException("Product Line not found",HttpStatus.NOT_FOUND)
        );

        var cat = this.productCategoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new BusinessException("Product category not found",HttpStatus.NOT_FOUND)
        );

        if (cat.getActive().equals(false)) throw new BusinessException("Category is inactive");
        if (line.getActive().equals(false)) throw new BusinessException("Line is inactive");

        var product = new Product();
        product.setPrice(dto.getPrice());
        product.setCost(dto.getCost());
        product.setActive(true);
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setNcm(dto.getNcm());
        product.setUnit(dto.getUnit());
        product.setCategory(cat);
        product.setLine(line);
        product.setCreatedAt(LocalDateTime.now(ZoneOffset.of("-03:00")));
        this.productRepository.save(product);
    }

    @Override
    public List<Product> getProducts(SearchProductDto p) {
        if (p.getName()     == null ) p.setName("");
        if (p.getCategory() == null ) p.setCategory("");
        if (p.getUnit()     == null ) p.setUnit("");
        if (p.getBrand()    == null ) p.setBrand("");
        if (p.getLine()    == null  ) p.setLine("");
        if (p.getNcm()      == null) p.setNcm("");


        if (p.getStartUpdateDate() != null && p.getEndRegisterDate() != null
                && p.getStartUpdateDate().isAfter(p.getEndRegisterDate())) {
            throw new BusinessException("Start date cannot be after end date in update date.", HttpStatus.BAD_REQUEST);
        }

        if (p.getStartRegisterDate() != null && p.getEndRegisterDate() != null
                && p.getStartRegisterDate().isAfter(p.getEndRegisterDate())) {
            throw new BusinessException("Start date cannot be after end date in creation date.", HttpStatus.BAD_REQUEST);
        }

        System.out.println(p);
        return this.productRepository.findByFilters(
                p.getId(),
                p.getName(),
                p.getPriceStart(),
                p.getPriceEnd(),
                p.getCostStart(),
                p.getCostEnd(),
                p.getCategory(),
                p.getUnit(),
                p.getBrand(),
                p.getStartUpdateDate(),
                p.getEndUpdateDate(),
                p.getStartRegisterDate(),
                p.getEndRegisterDate(),
                p.getActive(),
                p.getNcm(),
                p.getLine()
        );
    }

    @Override
    public void updateProduct(Long id, UpdateProductDto dto) {
        Product p = this.productRepository.findById(id).orElseThrow(()->
                new BusinessException("Product not found",HttpStatus.NOT_FOUND));

        if (dto.getCategoryId() != null){
            var cat = this.productCategoryRepository.findById(dto.getCategoryId()).orElseThrow(
                    () -> new BusinessException("Product category not found",HttpStatus.NOT_FOUND)
            );

            p.setCategory(cat);
        }

        if (dto.getLineId() != null){
            var line = this.productLineRepository.findById(dto.getLineId()).orElseThrow(
                    () -> new BusinessException("Product line not found",HttpStatus.NOT_FOUND)
            );

            p.setLine(line);
        }

        if (dto.getNcm()        != null) p.setNcm(dto.getNcm());
        if (dto.getBrand()      != null) p.setBrand(    dto.getBrand());
        if (dto.getUnit()       != null) p.setUnit(     dto.getUnit());
        if (dto.getName()       != null) p.setName(     dto.getName());
        if (dto.getPrice()      != null) p.setPrice(dto.getPrice());
        if (dto.getCost()       != null) p.setCost(dto.getCost());

        p.setUpdatedAt(LocalDateTime.now(ZoneOffset.of("-03:00")));
        p.setActive(true);
        this.productRepository.save(p);
    }

    @Override
    public void delete(Long id) {
        var p = this.productRepository.findById(id).orElseThrow(
                () -> new BusinessException("Product not found", HttpStatus.NOT_FOUND)
        );

        var stockByProduct = this.stockRepository.findProduct(p.getId());

        if (stockByProduct.isPresent() && stockByProduct.get().getCurrentQuantity() > 0){
            throw new BusinessException("The product cannot be deleted because it is still in stock.");
        }

        p.setActive(false);
        this.productRepository.save(p);
    }
}
