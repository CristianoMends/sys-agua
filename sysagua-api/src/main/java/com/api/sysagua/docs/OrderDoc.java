package com.api.sysagua.docs;

import com.api.sysagua.dto.order.CreateOrderDto;
import com.api.sysagua.dto.order.UpdateOrderDto;
import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.model.Order;
import com.api.sysagua.model.ProductOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Order Controller", description = "Controlador responsável pela gestão dos pedidos.")
public interface OrderDoc {
    @Operation(
            summary = "Criação de um novo pedido",
            description = "Este método cria um novo pedido a partir dos dados fornecidos no DTO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> create(CreateOrderDto dto);

    @Operation(
            summary = "Busca de pedidos por filtros",
            description = "Permite buscar por pedidos com base em filtros(cliente, entregador, etc)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<List<Order>> list(
            Long id,
            Customer customer,
            DeliveryPerson deliveryPerson,
            List<ProductOrder> productOrders,
            OrderStatus status,
            BigDecimal receivedAmount,
            BigDecimal totalAmount,
            PaymentMethod paymentMethod,
            LocalDateTime createdAt,
            LocalDateTime finishedAt
    );

    @Operation(
            summary = "Deletar um pedido",
            description = "Marca um pedido como inativo com base no ID."
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
            summary = "Atualizar os dados de um pedido",
            description = "Atualiza os dados de um pedido com base no ID informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> update(Long id, UpdateOrderDto dto);
}
