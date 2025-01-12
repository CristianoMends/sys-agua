package com.api.sysagua.service;

import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.SearchCustomerDto;
import com.api.sysagua.dto.customer.UpdateCustomerDto;
import com.api.sysagua.model.Customer;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {

    void createCustomer(CreateCustomerDto dto);

    List<Customer> findByFilters(SearchCustomerDto searchCustomerDto);

    void deleteCustomer(Long id);

    void updateCustomer(Long id, UpdateCustomerDto dto);
}
