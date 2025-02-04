package com.api.sysagua.service.impl;

import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.UpdateCustomerDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Customer;
import com.api.sysagua.repository.CustomerRepository;
import com.api.sysagua.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void createCustomer(CreateCustomerDto dto) {
        this.validatePhoneAndCnpj(dto.getPhone(),dto.getCnpj());

        var toSave = dto.toModel();
        toSave.setCreatedAt(LocalDate.now());
        toSave.setActive(true);
        repository.save(toSave);
    }

    @Override
    public List<Customer> findByFilters(Long id,
                                        String name,
                                        String phone,
                                        String street,
                                        String neighborhood,
                                        String city,
                                        String state,
                                        Boolean active,
                                        String cnpj) {

        return this.repository.findByFilters(
                id, name, street, neighborhood, city, state, phone, active, cnpj
        );
    }

    @Override
    public void deleteCustomer(Long id) {
        var customer = this.repository.findById(id).orElseThrow(
                () -> new BusinessException("Customer with id not found", HttpStatus.NOT_FOUND)
        );

        if (!customer.getActive()) {
            throw new BusinessException("Customer is already inactive");
        }

        customer.setActive(false);
        this.repository.save(customer);
    }


    @Override
    public void updateCustomer(Long id, UpdateCustomerDto dto) {
        var customer = this.repository.findById(id).orElseThrow(
                () -> new BusinessException("Customer with id not found", HttpStatus.NOT_FOUND)
        );

        this.validatePhoneAndCnpj(dto.getPhone(),dto.getCnpj());


        if (dto.getName() != null) customer.setName(dto.getName());
        if (dto.getPhone() != null) customer.setPhone(dto.getPhone());
        if (dto.getAddress().getNumber() != null) customer.getAddress().setNumber(dto.getAddress().getNumber());
        if (dto.getAddress().getStreet() != null) customer.getAddress().setStreet(dto.getAddress().getStreet());
        if (dto.getAddress().getCity() != null) customer.getAddress().setCity(dto.getAddress().getCity());
        if (dto.getAddress().getNeighborhood() != null)
            customer.getAddress().setNeighborhood(dto.getAddress().getNeighborhood());
        if (dto.getAddress().getState() != null) customer.getAddress().setState(dto.getAddress().getState());
        if (dto.getActive() != null) customer.setActive(dto.getActive());

        this.repository.save(customer);
    }

    private void validatePhoneAndCnpj(String phone, String cnpj){
        this.repository.findByCnpj(cnpj).ifPresent(
                c -> {
                    throw new BusinessException("There is already a customer with this CNPJ");
                }
        );

        this.repository.findByPhone(phone).ifPresent(
                c -> {
                    throw new BusinessException("There is already a customer with this PHONE");
                }
        );
    }
}
