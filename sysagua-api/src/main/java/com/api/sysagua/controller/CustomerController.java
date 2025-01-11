package com.api.sysagua.controller;

import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.SearchCustomerDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Customer;
import com.api.sysagua.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Customer Controller", description = "Controlador responsável pela gestão dos clientes.")
@RequestMapping("customers")
public class CustomerController {
    @Autowired
    private CustomerService service;

    @Operation(
            summary = "Criação de um novo cliente",
            description = "Este método cria um novo cliente a partir dos dados fornecidos no DTO.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cliente criado com sucesso.",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Não está autorizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid CreateCustomerDto dto
            ){
        this.service.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Busca de clientes por filtros",
            description = "Este método permite buscar clientes com base em vários filtros, como nome, telefone, endereço, etc.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Clientes encontrados com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Customer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados fornecidos são inválidos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Não está autorizado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Customer>> getByFilters(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Boolean active
    ) {
        var search = new SearchCustomerDto(
                id,name,street,neighborhood,city,state,phone, active
        );

        List<Customer> customers = service.findByFilters(search);
        return ResponseEntity.ok(customers);
    }
}
