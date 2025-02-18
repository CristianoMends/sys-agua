package com.api.sysagua.factory;

import com.api.sysagua.dto.supplier.CreateSupplierDto;
import com.api.sysagua.model.Supplier;

public class SupplierFactory {

    public static Supplier createSupplier(CreateSupplierDto dto) {
        Supplier supplier = new Supplier();
        supplier.setSocialReason(dto.getSocialReason());
        supplier.setCnpj(dto.getCnpj());
        supplier.setAddress(dto.getAddress());
        supplier.setPhone(dto.getPhone());
        supplier.setTradeName(dto.getTradeName());
        supplier.setStateRegistration(dto.getStateRegistration());
        supplier.setMunicipalRegistration(dto.getMunicipalRegistration());
        supplier.setActive(true);
        return supplier;
    }
}
