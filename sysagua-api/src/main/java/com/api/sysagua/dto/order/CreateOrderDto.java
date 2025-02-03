package com.api.sysagua.dto.order;

import com.api.sysagua.enumeration.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO para criação de um novo Pedido.")
public class CreateOrderDto {
    @Schema(description = "Id do cliente atrelado ao pedido", example = "1")
    @NotNull
    private Long customerId;

    @Schema(description = "Id do entregador atrelado ao pedido", example = "1")
    @NotNull
    private Long deliveryPersonId;

    @Schema(description = "Produtos pedidos")
    @NotNull(message = "Products is mandatory")
    private List<CreateProductOrderDto> productOrders;

    @Schema(description = "Valor recebido", example = "6.50")
    @NotNull(message = "ReceivedAmount is mandatory")
    private BigDecimal receivedAmount;

    @Schema(description = "Valor total, caso não informado, é calculado automatico", example = "14.00")
    private BigDecimal totalAmount;

    @Schema(description = "Metodo de pagamento", example = "PIX")
    @NotNull(message = "PaymentMethod is mandatory")
    private PaymentMethod paymentMethod;

}
