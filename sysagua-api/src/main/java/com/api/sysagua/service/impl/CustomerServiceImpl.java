package com.api.sysagua.service.impl;

import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.SearchCustomerDto;
import com.api.sysagua.model.Customer;
import com.api.sysagua.repository.CustomerRepository;
import com.api.sysagua.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void createCustomer(CreateCustomerDto dto) {
        var toSave = dto.toModel();
        toSave.setCreatedAt(LocalDate.now());
        toSave.setActive(true);
        repository.save(toSave);
    }

    @Override
    public List<Customer> findByFilters(SearchCustomerDto dto) {
        System.out.println(dto);

        return this.repository.findByFilters(
                dto.getId(),
                dto.getName(),
                dto.getPhone(),
                dto.getStreet(),
                dto.getNeighborhood(),
                dto.getCity(),
                dto.getState(),
                dto.getActive());
    }
}
