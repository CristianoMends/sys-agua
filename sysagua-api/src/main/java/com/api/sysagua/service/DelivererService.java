package com.api.sysagua.service;

import com.api.sysagua.dto.deliverer.CreateDelivererDto;
import com.api.sysagua.dto.deliverer.SearchDelivererDto;
import com.api.sysagua.dto.deliverer.UpdateDelivererDto;
import com.api.sysagua.model.Deliverer;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DelivererService {
    void createDeliverer(CreateDelivererDto dto);

    List<Deliverer> findByFilters(SearchDelivererDto searchDelivererDto);

    void deleteDeliverer(Long id);

    void updateDeliverer(Long id, UpdateDelivererDto dto);
}
