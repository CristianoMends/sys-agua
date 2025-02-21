package com.api.sysagua.dto.order;

import com.api.sysagua.dto.productItem.ViewProductItemDto;
import com.api.sysagua.dto.transaction.ViewTransactableDto;
import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para visualização de um Pedido")
public class ViewOrderDto extends ViewTransactableDto {

    @Schema(description = "ID do pedido", example = "1")
    private Long id;

    @Schema(description = "Status da entrega do pedido", example = "PENDING")
    private DeliveryStatus deliveryStatus;

    @Schema(description = "Status do pagamento do pedido", example = "PAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Valor recebido do pedido", example = "50.00")
    private BigDecimal paidAmount;

    @Schema(description = "Valor total do pedido", example = "80.00")
    private BigDecimal totalAmount;

    @Schema(description = "Saldo restante do pedido", example = "53.47")
    private BigDecimal balance;

    @Schema(description = "Método de pagamento utilizado", example = "PIX")
    private PaymentMethod paymentMethod;

    @Schema(description = "Data de criação do pedido", example = "2024-02-08T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Data de finalização do pedido", example = "2024-02-08T12:45:00")
    private LocalDateTime finishedAt;

    @Schema(description = "Data de cancelamento do pedido", example = "2024-02-08T13:00:00")
    private LocalDateTime canceledAt;

    @Schema(description = "Observações ou instruções adicionais sobre o pedido", example = "Entrega agendada para amanhã no período da tarde")
    private String description;

    @Schema(description = "Dados do cliente associado ao pedido")
    private Customer customer;

    @Schema(description = "Dados do entregador responsável pelo pedido")
    private DeliveryPerson deliveryPerson;

    @Schema(description = "Lista de produtos incluídos no pedido",
            implementation = ViewProductItemDto.class)
    private List<ViewProductItemDto> productOrders;
}
