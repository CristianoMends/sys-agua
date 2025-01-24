package com.api.sysagua.service;

import com.api.sysagua.dto.supplier.CreateSupplierDto;
import com.api.sysagua.dto.supplier.SearchSupplierDto;
import com.api.sysagua.dto.supplier.UpdateSupplierDto;
import com.api.sysagua.model.Supplier;

import java.util.List;

public interface SupplierService {

    void create(CreateSupplierDto dto);

    List<Supplier> list(SearchSupplierDto dto);

    void delete(Long id);

    void update(Long id, UpdateSupplierDto dto);

}
