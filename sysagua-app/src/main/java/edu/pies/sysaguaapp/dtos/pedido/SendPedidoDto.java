package edu.pies.sysaguaapp.dtos.pedido;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SendPedidoDto {
    private LocalDate dataPedido;
    private Long customerId;
    private Long deliveryPersonId;
    private List<ItemPedidoDto> productOrders;
    private BigDecimal receivedAmount;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private String description;

    public SendPedidoDto() {
        productOrders = new ArrayList<>();
    }
    public SendPedidoDto(List<ItemPedidoDto> productOrders, Long customerId, Long deliveryPersonId, BigDecimal totalAmount, PaymentMethod paymentMethod, String description, LocalDate dataPedido) {
        this.dataPedido = dataPedido;
        this.productOrders = productOrders;
        this.customerId = customerId;
        this.deliveryPersonId = deliveryPersonId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.description = description;
    }
}
