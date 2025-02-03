package com.api.sysagua.dto.order;

import com.api.sysagua.enumeration.OrderStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.model.ProductOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Schema(description = "Cliente atrelado ao pedido", example = "Andre", maxLength = 20)
    @NotBlank(message = "Customer is mandatory")
    @Size(max = 20, message = "Name should not exceed 20 characters")
    private Customer customer;

    @Schema(description = "Entregador atrelado ao pedido", example = "Hugo", maxLength = 20)
    @NotBlank(message = "DeliveryPerson is mandatory")
    @Size(max = 20, message = "Name should not exceed 20 characters")
    private DeliveryPerson deliveryPerson;

    @Schema(description = "Produtos pedidos", example = "Garrafao 20l", maxLength = 20)
    @NotBlank(message = "Products is mandatory")
    @Size(max = 20, message = "Product name should not exceed 20 characters")
    private List<ProductOrder> productOrders;

    @Schema(description = "Status do pedido", example = "pendente", maxLength = 20)
    @NotBlank(message = "Status is mandatory")
    @Size(max = 20, message = "Status should not exceed 20 characters")
    private OrderStatus status;

    @Schema(description = "Valor recebido", example = "6.50", maxLength = 20)
    @NotBlank(message = "ReceivedAmount is mandatory")
    @Size(max = 20, message = "Should not exceed 20 characters")
    private BigDecimal receivedAmount;

    @Schema(description = "Valor total", example = "14.00", maxLength = 20)
    @NotBlank(message = "TotalAmount is mandatory")
    @Size(max = 20, message = "Should not exceed 20 characters")
    private BigDecimal totalAmount;

    @Schema(description = "Metodo de pagamento", example = "Pix", maxLength = 20)
    @NotBlank(message = "PaymentMethod is mandatory")
    @Size(max = 20, message = "Should not exceed 20 characters")
    private PaymentMethod paymentMethod;




}
