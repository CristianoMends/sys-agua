package edu.pies.sysaguaapp.models.Pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;

import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;

import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.models.compras.ItemCompra;
import edu.pies.sysaguaapp.models.Transactable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido extends Transactable {
    private Long id;
    private Clientes customer;
    private Entregador deliveryPerson;
    private List<ItemPedido> productOrders;
    private PedidoStatus deliveryStatus;
    private BigDecimal totalAmount;
    private String enderecoEntrega;
    private LocalDateTime createdAt;
    private boolean active;
    private final String type = "order";


    public Pedido(){
        productOrders = new ArrayList<>();
    }

    public Pedido(LocalDateTime createdAt){
       this.createdAt = createdAt;
       productOrders = new ArrayList<>();
    }

    public boolean getActive(){
        return active;
    }

    @Override
    public void calculateTotalAmount() {
        if (getTotalAmount() != null) return;

        this.totalAmount = productOrders.stream()
                .map(p -> p.getUnitPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
