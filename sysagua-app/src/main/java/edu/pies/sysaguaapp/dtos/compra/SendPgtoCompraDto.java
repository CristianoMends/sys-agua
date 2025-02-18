package edu.pies.sysaguaapp.dtos.compra;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
public class SendPgtoCompraDto {
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String description;
    private Long idCompra;
}
