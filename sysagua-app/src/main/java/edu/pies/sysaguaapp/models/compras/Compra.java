package edu.pies.sysaguaapp.models.compras;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;
import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.models.Pedido.ItemPedido;
import edu.pies.sysaguaapp.models.Transactable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter @Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Compra extends Transactable {
    private Long id;
    private Boolean active;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<ItemCompra> items;
    private Fornecedor supplier;
    private BigDecimal paidAmount;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private LocalDateTime entryAt;
    private LocalDateTime canceledAt;
    private LocalDateTime finishedAt;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String nfe;
    private String description;
    private final String type = "purchase";


    public Compra() {
        items = new ArrayList<>();
    }

    public Compra(LocalDateTime entryAt) {
        this.entryAt = entryAt;
        items = new ArrayList<>();
    }

    @Override
    public void calculateTotalAmount() {
        if (getTotalAmount() != null) return;

        this.totalAmount = items.stream()
                .map(i -> i.getPurchasePrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
