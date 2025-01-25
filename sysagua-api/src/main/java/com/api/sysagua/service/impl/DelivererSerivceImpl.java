package com.api.sysagua.service.impl;

import com.api.sysagua.dto.deliverer.CreateDelivererDto;
import com.api.sysagua.dto.deliverer.SearchDelivererDto;
import com.api.sysagua.dto.deliverer.UpdateDelivererDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Deliverer;
import com.api.sysagua.repository.DelivererRepository;
import com.api.sysagua.service.DelivererService;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DelivererSerivceImpl implements DelivererService{

    @Autowired
    private DelivererRepository repository;

    @Override
    public void createDeliverer(CreateDelivererDto dto) {
        var d = this.repository.findByFilters(
                null,
                null,
                null);

        if (!d.isEmpty()) {
            throw new BusinessException("There is already a deliverer name and phone number");
        }

        var toSave = dto.toModel();
        toSave.setCreatedAt(LocalDate.now());
        toSave.setActive(true);
        repository.save(toSave);
    }

    @Override
    public List<Deliverer> findByFilters(SearchDelivererDto dto){
        return this.repository.findByFilters(
                dto.getId(),
                dto.getName(),
                dto.getPhone());
    }

    @Override
    public void deleteDeliverer(Long id){
        var d = this.repository.findById(id).orElseThrow(() -> new BusinessException("No deliverer with specified ID was found", HttpStatus.NOT_FOUND));
        if (!d.getActive()) {
            throw new BusinessException("Deliverer is already inactive");
        }

        d.setActive(false);
        this.repository.save(d);
    }

    @Override
    public void updateDeliverer(Long id, UpdateDelivererDto dto){
        var d = this.repository.findByFilters(
                id,
                null,
                null);

        if (d.isEmpty()){
            throw new BusinessException("Deliverer with id not found", HttpStatus.NOT_FOUND);
        }

        var deliverer = d.getFirst();

        if (dto.getName() != null) deliverer.setName(dto.getName());
        if (dto.getPhone() != null ) deliverer.setPhone(dto.getPhone());

        this.repository.save(deliverer);
    }
}
