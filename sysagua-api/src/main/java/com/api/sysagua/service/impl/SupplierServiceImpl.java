package com.api.sysagua.service.impl;

import com.api.sysagua.dto.supplier.CreateSupplierDto;
import com.api.sysagua.dto.supplier.SearchSupplierDto;
import com.api.sysagua.dto.supplier.UpdateSupplierDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Supplier;
import com.api.sysagua.repository.SupplierRepository;
import com.api.sysagua.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public void create(CreateSupplierDto dto) {
        var supplier = this.supplierRepository.findByCnpj(dto.getCnpj());

        if (supplier.isPresent()){
            throw new BusinessException(String.format("Supplier with cnpj %s has exists",dto.getCnpj()));
        }

        supplier = this.supplierRepository.findByPhone(dto.getPhone());

        if (supplier.isPresent()){
            throw new BusinessException(String.format("Supplier with phone %s has exists",dto.getPhone()));
        }
        var supplierToSave = dto.toModel();
        supplierToSave.setActive(true);
        this.supplierRepository.save(supplierToSave);
    }

    @Override
    public List<Supplier> list(SearchSupplierDto dto) {
        if (dto.getCnpj() == null) dto.setCnpj("");
        if (dto.getPhone() == null) dto.setPhone("");
        if (dto.getSocialReason() == null) dto.setSocialReason("");

        return this.supplierRepository.findByFilters(dto.getId(),dto.getSocialReason(),dto.getCnpj(),dto.getPhone(), dto.getActive());
    }

    @Override
    public void delete(Long id) {
        var supplier = this.supplierRepository.findById(id).orElseThrow(()-> new BusinessException("Supplier not found", HttpStatus.NOT_FOUND));
        if (supplier.getActive().equals(false)) throw  new BusinessException("supplier is already inactive");
        supplier.setActive(false);
        this.supplierRepository.save(supplier);
    }

    @Override
    public void update(Long id, UpdateSupplierDto dto) {
        var supplier = this.supplierRepository.findById(id).orElseThrow(() -> new BusinessException("Supplier not found",HttpStatus.NOT_FOUND));

        if (dto.getCnpj() != null) supplier.setCnpj(dto.getCnpj());
        if (dto.getSocialReason() != null) supplier.setSocialReason(dto.getSocialReason());
        if (dto.getPhone() != null) supplier.setPhone(dto.getPhone());
        if (dto.getCity() != null) supplier.getAddress().setCity(dto.getCity());
        if (dto.getState() != null) supplier.getAddress().setState(dto.getState());
        if (dto.getNumber() != null) supplier.getAddress().setNumber(dto.getNumber());
        if (dto.getStreet() != null) supplier.getAddress().setStreet(dto.getStreet());
        if (dto.getNeighborhood() != null) supplier.getAddress().setNeighborhood(dto.getNeighborhood());

        this.supplierRepository.save(supplier);
    }
}
