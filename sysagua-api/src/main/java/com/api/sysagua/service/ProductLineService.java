package com.api.sysagua.service;

import com.api.sysagua.model.ProductLine;

import java.util.List;

public interface ProductLineService {

    List<ProductLine> list(Long id, String name, Boolean active);

    void create(String name);

    void delete(Long id);

}
