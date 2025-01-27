package com.api.sysagua.controller;

import com.api.sysagua.docs.DelivererDoc;
import com.api.sysagua.dto.deliverer.CreateDelivererDto;
import com.api.sysagua.dto.deliverer.SearchDelivererDto;
import com.api.sysagua.dto.deliverer.UpdateDelivererDto;
import com.api.sysagua.model.Deliverer;
import com.api.sysagua.service.DelivererService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("deliverers")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Deliverer Controller", description = "Endpoints para gerenciamento de entregadores")
public class DelivererController implements DelivererDoc{

    @Autowired
    private DelivererService service;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateDelivererDto dto){
        this.service.createDeliverer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<List<Deliverer>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone){

        var search = new SearchDelivererDto(id, name, phone);

        List<Deliverer> deliverers = service.findByFilters(search);
        return ResponseEntity.ok(deliverers);
    }

    @CrossOrigin
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.service.deleteDeliverer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateDelivererDto dto){
        this.service.updateDeliverer(id, dto);
        return ResponseEntity.noContent().build();
    }
}
