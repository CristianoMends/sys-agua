package com.api.sysagua.docs;

import com.api.sysagua.dto.deliveryPerson.CreateDeliveryPersonDto;
import com.api.sysagua.dto.deliveryPerson.UpdateDeliveryPersonDto;
import com.api.sysagua.exception.ResponseError;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryPersonDoc {

    @Operation(
            summary = "Criação de um novo entregador",
            description = "Este método cria um novo entregador a partir dos dados fornecidos no DTO.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Entregador criado com sucesso.",
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
    ResponseEntity<Void> create(CreateDeliveryPersonDto dto);

    @Operation(
            summary = "Busca de entregadores por filtros",
            description = "Este método permite buscar entregadores com base em filtros como nome e telefone.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Entregador encontrado com sucesso",
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
    ResponseEntity<List<DeliveryPerson>> list(
            Long id,
            String name,
            String phone,
            Boolean active,
            LocalDate createdAtStart,
            LocalDate createdAtEnd
            );

    @Operation(
            summary = "Deletar (inativar) um entregador",
            description = "Marca o entregador como inativo com base no ID fornecido.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Entregador deletado (inativado) com sucesso.",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Entregador não encontrado.",
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
            summary = "Atualizar informações de um entregador",
            description = "Atualiza as informações de um entregador com base no ID fornecido. Os dados a serem atualizados são fornecidos no corpo da requisição.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Entregador atualizado com sucesso.",
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
                            description = "Entregador não encontrado.",
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
    ResponseEntity<Void> update(Long id, UpdateDeliveryPersonDto dto);
}
