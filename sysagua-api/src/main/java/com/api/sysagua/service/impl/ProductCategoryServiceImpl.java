package com.api.sysagua.service.impl;

import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.ProductCategory;
import com.api.sysagua.repository.ProductCategoryRepository;
import com.api.sysagua.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> list(Long id, String name, Boolean active) {
        if (name == null) name = "";
        return this.productCategoryRepository.list(id,active,name);
    }

    @Override
    public void create(String name) {
        var category = new ProductCategory();
        category.setName(name);
        category.setActive(true);
        this.productCategoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        var cat = this.productCategoryRepository.findById(id).orElseThrow(
                ()-> new BusinessException("Category not found", HttpStatus.NOT_FOUND)
        );

        if (cat.getActive().equals(false)) throw new BusinessException("Category is inactive");

        cat.setActive(false);
        this.productCategoryRepository.save(cat);
    }
}
