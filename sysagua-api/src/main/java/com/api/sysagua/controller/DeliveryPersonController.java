package com.api.sysagua.controller;

import com.api.sysagua.docs.DeliveryPersonDoc;
import com.api.sysagua.dto.deliveryPerson.CreateDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.SearchDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.UpdateDeliveryPersonDto;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.service.DeliveryPersonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("deliveryPersons")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "DeliveryPerson Controller", description = "Endpoints para gerenciamento de entregadores")
public class DeliveryPersonController implements DeliveryPersonDoc{

    @Autowired
    private DeliveryPersonService service;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateDeliveryPersonDto dto){
        this.service.createDeliveryPerson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<DeliveryPerson>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) LocalDate createdAtStart,
            @RequestParam(required = false) LocalDate createdAtEnd
            ){

        var search = new SearchDeliveryPersonDto(id, name, phone, active, createdAtStart, createdAtEnd);

        List<DeliveryPerson> deliverers = service.findByFilters(search);
        return ResponseEntity.ok(deliverers);
    }

    @CrossOrigin
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.service.deleteDeliveryPerson(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateDeliveryPersonDto dto){
        this.service.updateDeliveryPerson(id, dto);
        return ResponseEntity.noContent().build();
    }
}
