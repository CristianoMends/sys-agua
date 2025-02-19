package edu.pies.sysaguaapp.models.Pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;

import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;

import edu.pies.sysaguaapp.models.Fornecedor;
import edu.pies.sysaguaapp.models.compras.ItemCompra;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Pedido {
    private Long id;
    private Clientes customer;
    private Entregador deliveryPerson;
    private List<ItemPedido> productOrders;
    private PedidoStatus deliveryStatus;
    private BigDecimal receivedAmount;
    private BigDecimal totalAmount;
    private BigDecimal balance;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;
    private String enderecoEntrega;
    private boolean active;
    private String type;


    //compras
    private LocalDateTime updatedAt;
    private List<ItemCompra> items;
    private Fornecedor supplier;
    private String nfe;
    private LocalDateTime entryAt;
    private BigDecimal paidAmount;
    private String description;


    public Pedido(){
        productOrders = new ArrayList<>();
    }

    public Pedido(LocalDateTime entryAt){
       this.entryAt = entryAt;
       productOrders = new ArrayList<>();
    }

    public boolean getActive(){
        return active;
    }
}
