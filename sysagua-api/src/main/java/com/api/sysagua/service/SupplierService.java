package com.api.sysagua.service;

import com.api.sysagua.dto.supplier.CreateSupplierDto;
import com.api.sysagua.dto.supplier.UpdateSupplierDto;
import com.api.sysagua.model.Supplier;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SupplierService {

    void create(CreateSupplierDto dto);

    List<Supplier> list(Long id, String socialReason, String cnpj, String phone, Boolean active);

    void delete(Long id);

    void update(Long id, UpdateSupplierDto dto);

}
