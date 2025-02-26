package com.api.sysagua.service.impl;

import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.ProductLine;
import com.api.sysagua.repository.ProductLineRepository;
import com.api.sysagua.service.ProductLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductLineServiceImpl implements ProductLineService {
    @Autowired
    private ProductLineRepository productLineRepository;

    @Override
    public List<ProductLine> list(Long id, String name, Boolean active) {
        if (name == null) name = "";
        return this.productLineRepository.list(id,active,name);
    }

    @Override
    public void create(String name) {
        var category = new ProductLine();
        category.setName(name);
        category.setActive(true);
        this.productLineRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        var cat = this.productLineRepository.findById(id).orElseThrow(
                ()-> new BusinessException("Category not found", HttpStatus.NOT_FOUND)
        );

        if (cat.getActive().equals(false)) throw new BusinessException("Category is inactive");

        cat.setActive(false);
        this.productLineRepository.save(cat);
    }
}
