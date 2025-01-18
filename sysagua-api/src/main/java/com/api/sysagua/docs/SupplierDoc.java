package com.api.sysagua.docs;

import com.api.sysagua.dto.supplier.CreateSupplierDto;
import com.api.sysagua.dto.supplier.UpdateSupplierDto;
import com.api.sysagua.model.Supplier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface SupplierDoc {

    @Operation(summary = "Cria um novo fornecedor",
            description = "Recebe as informações de um fornecedor e o cadastra no sistema.")
    @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso.")
    @PostMapping
    @CrossOrigin
    ResponseEntity<Void> create(
            @RequestBody @Valid CreateSupplierDto dto);

    @Operation(summary = "Lista fornecedores",
            description = "Retorna uma lista de fornecedores com base nos parâmetros informados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de fornecedores retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos."),
            @ApiResponse(responseCode = "403", description = "Não autorizado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    @CrossOrigin
    ResponseEntity<List<Supplier>> list(
            @Parameter(description = "Identificador do fornecedor")
            @RequestParam(value = "id", required = false) Long id,
            @Parameter(description = "Razão social do fornecedor")
            @RequestParam(value = "socialReason", required = false) String socialReason,
            @Parameter(description = "CNPJ do fornecedor")
            @RequestParam(value = "cnpj", required = false) String cnpj,
            @Parameter(description = "Telefone do fornecedor")
            @RequestParam(value = "phone", required = false) String phone,
            @Parameter(description = "Status ativo do fornecedor")
            @RequestParam(value = "active", required = false) Boolean active);

    @Operation(summary = "Deleta um fornecedor",
            description = "Remove um fornecedor do sistema com base no ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Fornecedor deletado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Não autorizado."),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    @DeleteMapping("{id}")
    @CrossOrigin
    ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único do fornecedor")
            @PathVariable Long id);

    @Operation(summary = "Atualiza um fornecedor",
            description = "Atualiza as informações de um fornecedor existente com base no ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Fornecedor atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado."),
            @ApiResponse(responseCode = "403", description = "Não autorizado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização.")
    })
    @PutMapping("{id}")
    @CrossOrigin
    ResponseEntity<Void> update(
            @Parameter(description = "Identificador único do fornecedor")
            @PathVariable Long id,
            @Valid @RequestBody UpdateSupplierDto dto);

}
