package com.api.sysagua.service;

import com.api.sysagua.dto.deliveryPerson.CreateDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.SearchDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.UpdateDeliveryPersonDto;
import com.api.sysagua.model.DeliveryPerson;

import java.util.List;
public interface DeliveryPersonService {
    void createDeliveryPerson(CreateDeliveryPersonDto dto);

    List<DeliveryPerson> findByFilters(SearchDeliveryPersonDto searchDelivererDto);

    void deleteDeliveryPerson(Long id);

    void updateDeliveryPerson(Long id, UpdateDeliveryPersonDto dto);
}
