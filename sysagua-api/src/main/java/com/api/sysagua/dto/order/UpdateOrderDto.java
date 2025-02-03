package com.api.sysagua.dto.order;

import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.model.ProductOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDto {

    @Schema(description = "Id do pedido", example = "4213")
    private Long id;

    @Schema(description = "Cliente para o qual o pedido é destinado.", example = "Joao")
    private Customer customer;

    @Schema(description = "Entregador responsável pelo pedido", example = "Pedro")
    private DeliveryPerson deliveryPerson;

    @Schema(description = "Produtos pedidos", example = "Garrafao 20l")
    private List<ProductOrder> productOrders;

    @Schema(description = "Status do pedido", example = "realizado")
    private OrderStatus status;

    @Schema(description = "Valor recebido do pedido", example = "50.00")
    private BigDecimal receivedAmount;

    @Schema(description = "Valor total do pedido", example = "80.00")
    private BigDecimal totalAmount;

}
