package com.api.sysagua.service;

import com.api.sysagua.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> list(Long id, String name, Boolean active);

    void create(String name);

    void delete(Long id);
}
