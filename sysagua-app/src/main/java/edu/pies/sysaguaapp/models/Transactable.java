package edu.pies.sysaguaapp.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.models.compras.Compra;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Pedido.class, name = "order"),
    @JsonSubTypes.Type(value = Compra.class, name = "purchase")
})
public abstract class Transactable {
    private Long id;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;
    private String description;

    public abstract void calculateTotalAmount();
}
