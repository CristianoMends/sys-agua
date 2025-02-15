package com.api.sysagua.docs;

import com.api.sysagua.dto.supplier.CreateSupplierDto;
import com.api.sysagua.dto.supplier.UpdateSupplierDto;
import com.api.sysagua.model.Supplier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Supplier Controller", description = "Controlador responsável pela gestão dos fornecedores.")
public interface SupplierDoc {

    @Operation(summary = "Cria um novo fornecedor",
            description = "Recebe as informações de um fornecedor e o cadastra no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @PostMapping
    @CrossOrigin
    ResponseEntity<Void> create(
            @RequestBody @Valid CreateSupplierDto dto);

    @Operation(summary = "Lista fornecedores",
            description = "Retorna uma lista de fornecedores com base nos parâmetros informados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @GetMapping
    @CrossOrigin
    ResponseEntity<List<Supplier>> list(
            @Parameter(description = "Identificador do fornecedor")
            Long id,
            @Parameter(description = "Razão social do fornecedor")
            String socialReason,
            @Parameter(description = "CNPJ do fornecedor")
            String cnpj,
            @Parameter(description = "Telefone do fornecedor")
            String phone,
            @Parameter(description = "Status ativo do fornecedor")
            Boolean active,
            @Parameter(description = "Nome fantasia")
            String tradeName,
            @Parameter(description = "Inscrição estadual")
            String stateRegistration,
            @Parameter(description = "Inscrição municipal")
            String municipalRegistration
    );

    @Operation(summary = "Deleta um fornecedor",
            description = "Remove um fornecedor do sistema com base no ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @DeleteMapping("{id}")
    @CrossOrigin
    ResponseEntity<Void> delete(
            @Parameter(description = "Identificador único do fornecedor")
            @PathVariable Long id);

    @Operation(summary = "Atualiza um fornecedor",
            description = "Atualiza as informações de um fornecedor existente com base no ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @PutMapping("{id}")
    @CrossOrigin
    ResponseEntity<Void> update(
            @Parameter(description = "Identificador único do fornecedor")
            @PathVariable Long id,
            @Valid @RequestBody UpdateSupplierDto dto);

}
