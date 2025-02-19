package edu.pies.sysaguaapp.models.Pedido;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.TransactionType;
import edu.pies.sysaguaapp.models.User;
import edu.pies.sysaguaapp.models.compras.Compra;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionPedido {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private User responsibleUser;
    private Pedido transactable;
    private PaymentMethod paymentMethod;
}
