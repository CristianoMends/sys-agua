package com.api.sysagua.service.impl;

import com.api.sysagua.dto.deliveryPerson.CreateDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.SearchDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.UpdateDeliveryPersonDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.repository.DeliveryPersonRepository;
import com.api.sysagua.service.DeliveryPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeliveryPersonSerivceImpl implements DeliveryPersonService {

    @Autowired
    private DeliveryPersonRepository repository;

    @Override
    public void createDeliveryPerson(CreateDeliveryPersonDto dto) {
        var d = this.repository.findByPhone(dto.getPhone());

        if (d.isPresent()) {
            throw new BusinessException("There is already a deliveryPerson name and phone number");
        }

        var toSave = new DeliveryPerson();
        toSave.setCreatedAt(LocalDate.now());
        toSave.setName(dto.getName());
        toSave.setPhone(dto.getPhone());
        toSave.setActive(true);
        repository.save(toSave);
    }

    @Override
    public List<DeliveryPerson> findByFilters(SearchDeliveryPersonDto dto){
        return this.repository.findByFilters(
                dto.getId(),
                dto.getName(),
                dto.getPhone(),
                dto.getActive(),
                dto.getCreatedAtStart(),
                dto.getCreatedAtEnd()
        );
    }

    @Override
    public void deleteDeliveryPerson(Long id){
        var d = this.repository.findById(id).orElseThrow(() -> new BusinessException("No deliveryPerson with specified ID was found", HttpStatus.NOT_FOUND));
        if (!d.getActive()) {
            throw new BusinessException("DeliveryPerson is already inactive");
        }

        d.setActive(false);
        this.repository.save(d);
    }

    @Override
    public void updateDeliveryPerson(Long id, UpdateDeliveryPersonDto dto){
        var deliveryPerson = this.repository.findById(id).orElseThrow(
                ()-> new BusinessException("DeliveryPerson with id not found", HttpStatus.NOT_FOUND)
        );


        if (dto.getName() != null) deliveryPerson.setName(dto.getName());
        if (dto.getPhone() != null ) deliveryPerson.setPhone(dto.getPhone());

        this.repository.save(deliveryPerson);
    }
}
