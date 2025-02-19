package edu.pies.sysaguaapp.models.compras;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.TransactionType;
import edu.pies.sysaguaapp.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class TransactionCompra {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private User responsibleUser;
    private Compra transactable;
    private PaymentMethod paymentMethod;
}
