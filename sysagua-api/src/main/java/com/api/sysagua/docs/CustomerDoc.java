package com.api.sysagua.docs;

import com.api.sysagua.dto.customer.CreateCustomerDto;
import com.api.sysagua.dto.customer.UpdateCustomerDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CustomerDoc {

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
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro inesperado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    ResponseEntity<Void> create(CreateCustomerDto dto);

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
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro inesperado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
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
            description = "Marca o cliente como inativo com base no ID fornecido.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Cliente deletado (inativado) com sucesso.",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Não autorizado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro inesperado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    ResponseEntity<Void> delete(Long id);

    @Operation(
            summary = "Atualizar informações de um cliente",
            description = "Atualiza as informações de um cliente com base no ID fornecido. Os dados a serem atualizados são fornecidos no corpo da requisição.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Cliente atualizado com sucesso.",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Não autorizado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro inesperado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseError.class)
                            )
                    )
            }
    )
    ResponseEntity<Void> update(Long id, UpdateCustomerDto dto);
}
