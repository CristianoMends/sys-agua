package com.api.sysagua.dto.order;

import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Customer;
import com.api.sysagua.model.DeliveryPerson;
import com.api.sysagua.model.Order;
import com.api.sysagua.repository.CustomerRepository;
import com.api.sysagua.repository.DeliveryPersonRepository;
import com.api.sysagua.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
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


    @Schema(description = "Cliente para o qual o pedido é destinado.", example = "Joao")
    private Long customerId;

    @Schema(description = "Entregador responsável pelo pedido", example = "Pedro")
    private Long deliveryPersonId;

    /*@Schema(description = "Produtos pedidos", example = "Garrafao 20l")
    private List<CreateProductOrderDto> productOrder;
*/
    @Schema(description = "Status do pedido", example = "FINISHED")
    private DeliveryStatus status;

    @Schema(description = "Valor recebido do pedido", example = "50.00")
    private BigDecimal receivedAmount;

    @Schema(description = "Valor total do pedido", example = "80.00")
    private BigDecimal totalAmount;

    @Schema(description = "Método de pagamento do pedido", example = "MONEY")
    private PaymentMethod paymentMethod;

    @Schema(description = "Descricao do pedido", example = "Entregar para filho do Matheus")
    private String description;

}
