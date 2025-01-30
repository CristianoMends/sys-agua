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
        var c = this.repository.findByFilters(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                dto.getCnpj());

        if (!c.isEmpty()) {
            throw new BusinessException("There is already a customer with this CNPJ");
        }

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
        var c = this.repository.findById(id).orElseThrow(() -> new BusinessException("No customer with specified ID was found", HttpStatus.NOT_FOUND));
        if (!c.getActive()) {
            throw new BusinessException("Customer is already inactive");
        }

        c.setActive(false);
        this.repository.save(c);
    }


    @Override
    public void updateCustomer(Long id, UpdateCustomerDto dto) {
        var c = this.repository.findByFilters(
                id,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        if (c.isEmpty()) {
            throw new BusinessException("Customer with id not found", HttpStatus.NOT_FOUND);
        }

        var customer = c.getFirst();

        if (dto.getName() != null) customer.setName(dto.getName());
        if (dto.getPhone() != null) customer.setPhone(dto.getPhone());
        if (dto.getNumber() != null) customer.getAddress().setNumber(dto.getNumber());
        if (dto.getStreet() != null) customer.getAddress().setStreet(dto.getStreet());
        if (dto.getCity() != null) customer.getAddress().setCity(dto.getCity());
        if (dto.getNeighborhood() != null) customer.getAddress().setNeighborhood(dto.getNeighborhood());
        if (dto.getState() != null) customer.getAddress().setState(dto.getState());
        if (dto.getActive() != null) customer.setActive(dto.getActive());

        this.repository.save(customer);
    }
}
