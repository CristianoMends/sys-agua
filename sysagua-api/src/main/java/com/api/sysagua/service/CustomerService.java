package com.api.sysagua.service;

import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.UpdateCustomerDto;
import com.api.sysagua.model.Customer;

import java.util.List;

public interface CustomerService {

    void createCustomer(CreateCustomerDto dto);

    List<Customer> findByFilters(Long id,
                                 String name,
                                 String phone,
                                 String street,
                                 String neighborhood,
                                 String city,
                                 String state,
                                 Boolean active,
                                 String cnpj);

    void deleteCustomer(Long id);

    void updateCustomer(Long id, UpdateCustomerDto dto);
}
