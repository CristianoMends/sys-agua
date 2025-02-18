package edu.pies.sysaguaapp.models.Pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;

import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido {
    private Long id;
    private Clientes customer;
    private Entregador deliveryPerson;
    private List<ItemPedido> productOrders;
    private PedidoStatus deliveryStatus;
    private BigDecimal receivedAmount;
    private BigDecimal totalAmount;
    private LocalDateTime entryAt;

    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;

    private String description;


    private String enderecoEntrega;
    private boolean active;

    public Pedido(){
        productOrders = new ArrayList<>();
    }

    public Pedido(LocalDateTime entryAt){
       this.entryAt = entryAt;
       productOrders = new ArrayList<>();
    }

    public void setActive(boolean active){
        this.active = active;
    }
    public boolean getActive(){
        return active;
    }
}
