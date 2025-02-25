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
    private Long customerId;
    private Long deliveryPersonId;
    private List<ItemPedidoDto> productOrders;
    private BigDecimal paidAmount;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private String description;

    public SendPedidoDto() {
        productOrders = new ArrayList<>();
    }

}
