package edu.pies.sysaguaapp.dtos.pedido;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SendPgtoPedidoDto {
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String description;
}