package edu.pies.sysaguaapp.dtos.pedido;

import edu.pies.sysaguaapp.dtos.compra.ItemCompraDto;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.models.Produto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SendPedidoDto {
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
    public SendPedidoDto(List<ItemPedidoDto> productOrders, Long customerId) {
        this.productOrders = productOrders;
        this.customerId = customerId;
    }
}
