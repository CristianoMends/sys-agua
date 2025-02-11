package edu.pies.sysaguaapp.models.compras;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;
import edu.pies.sysaguaapp.models.Fornecedor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class Compra {
    private Long id;
    private BigDecimal totalValue;
    private Boolean active;
    private String updatedAt;
    private String createdAt;
    private List<ItemCompra> items;
    private Fornecedor supplier;
    private BigDecimal paidAmount;
    private BigDecimal totalAmount;
    private String entryAt;
    private String canceledAt;
    private String finishedAt;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String nfe;
    private String description;


    public Compra() {
        items = new ArrayList<>();
    }

    public Compra(String createdAt) {
        this.createdAt = createdAt;
        items = new ArrayList<>();
    }
}
