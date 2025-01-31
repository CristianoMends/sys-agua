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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@Tag(name = "DeliveryPerson Controller", description = "Endpoints para gerenciamento de entregadores")
public interface DeliveryPersonDoc {

    @Operation(
            summary = "Criação de um novo entregador",
            description = "Este método cria um novo entregador a partir dos dados fornecidos no DTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> create(CreateDeliveryPersonDto dto);

    @Operation(
            summary = "Busca de entregadores por filtros",
            description = "Este método permite buscar entregadores com base em filtros como nome e telefone."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
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
            description = "Marca o entregador como inativo com base no ID fornecido."
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
            summary = "Atualizar informações de um entregador",
            description = "Atualiza as informações de um entregador com base no ID fornecido. Os dados a serem atualizados são fornecidos no corpo da requisição."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> update(Long id, UpdateDeliveryPersonDto dto);
}
