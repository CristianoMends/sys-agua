package com.api.sysagua.service;

import com.api.sysagua.dto.deliveryPerson.CreateDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.UpdateDeliveryPersonDto;
import com.api.sysagua.model.DeliveryPerson;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryPersonService {
    void createDeliveryPerson(CreateDeliveryPersonDto dto);

    List<DeliveryPerson> findByFilters(Long id,
                                       String name,
                                       String phone,
                                       Boolean active,
                                       LocalDate createdAtStart,
                                       LocalDate createdAtEnd);

    void deleteDeliveryPerson(Long id);

    void updateDeliveryPerson(Long id, UpdateDeliveryPersonDto dto);
}
