package com.api.sysagua.docs;

import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.UpdateCustomerDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "Customer Controller", description = "Controlador responsável pela gestão dos clientes.")
public interface CustomerDoc {

    @Operation(
            summary = "Criação de um novo cliente",
            description = "Este método cria um novo cliente a partir dos dados fornecidos no DTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> create(CreateCustomerDto dto);

    @Operation(
            summary = "Busca de clientes por filtros",
            description = "Este método permite buscar clientes com base em vários filtros, como nome, telefone, endereço, etc."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<List<Customer>> list(
            Long id,
            String name,
            String phone,
            String street,
            String neighborhood,
            String city,
            String state,
            Boolean active,
            String cnpj
    );

    @Operation(
            summary = "Deletar (inativar) um cliente",
            description = "Marca o cliente como inativo com base no ID fornecido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> delete(Long id);

    @Operation(
            summary = "Atualizar informações de um cliente",
            description = "Atualiza as informações de um cliente com base no ID fornecido. Os dados a serem atualizados são fornecidos no corpo da requisição."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> update(Long id, UpdateCustomerDto dto);
}
