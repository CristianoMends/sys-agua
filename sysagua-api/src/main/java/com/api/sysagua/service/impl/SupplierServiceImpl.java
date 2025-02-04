package com.api.sysagua.service.impl;

import com.api.sysagua.dto.supplier.CreateSupplierDto;
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
        this.validatePhoneAndCnpj(dto.getPhone(), dto.getCnpj());

        var supplierToSave = dto.toModel();
        supplierToSave.setActive(true);
        this.supplierRepository.save(supplierToSave);
    }

    @Override
    public List<Supplier> list(Long id, String socialReason, String cnpj, String phone, Boolean active) {

        if (cnpj == null) cnpj = "";
        if (phone == null) phone = "";
        if (socialReason == null) socialReason = "";

        return this.supplierRepository.findByFilters(id,socialReason,cnpj,phone,active);
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

        if (!supplier.getCnpj().equals(dto.getCnpj())){
            this.supplierRepository.findByCnpj(dto.getCnpj()).ifPresent(c ->{
                throw new BusinessException("There is already a supplier with this CNPJ");
            });
        }
        if (!supplier.getPhone().equals(dto.getPhone())){
            this.supplierRepository.findByPhone(dto.getPhone()).ifPresent(c ->{
                throw new BusinessException("There is already a supplier with this Phone");
            });
        }
        if (dto.getCnpj() != null) supplier.setCnpj(dto.getCnpj());
        if (dto.getSocialReason() != null) supplier.setSocialReason(dto.getSocialReason());
        if (dto.getPhone() != null) supplier.setPhone(dto.getPhone());
        if (dto.getAddress().getCity() != null) supplier.getAddress().setCity(dto.getAddress().getCity());
        if (dto.getAddress().getState() != null) supplier.getAddress().setState(dto.getAddress().getState());
        if (dto.getAddress().getNumber() != null) supplier.getAddress().setNumber(dto.getAddress().getNumber());
        if (dto.getAddress().getStreet() != null) supplier.getAddress().setStreet(dto.getAddress().getStreet());
        if (dto.getAddress().getNeighborhood() != null) supplier.getAddress().setNeighborhood(dto.getAddress().getNeighborhood());

        this.supplierRepository.save(supplier);
    }

    private void validatePhoneAndCnpj(String phone, String cnpj){
        this.supplierRepository.findByCnpj(cnpj).ifPresent(
                c -> {
                    throw new BusinessException("There is already a customer with this CNPJ");
                }
        );

        this.supplierRepository.findByPhone(phone).ifPresent(
                c -> {
                    throw new BusinessException("There is already a customer with this PHONE");
                }
        );
    }
}
