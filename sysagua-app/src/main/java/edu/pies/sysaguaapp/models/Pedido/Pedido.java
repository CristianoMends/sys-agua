package edu.pies.sysaguaapp.models.Pedido;

import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;

import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatusPagamento;
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

public class Pedido {
    private Long id;
    private LocalDateTime dataPedido;
    private Clientes cliente;
    private Entregador entregador;
    private List<ItemPedido> items;
    private PaymentMethod paymentMethod;
    private BigDecimal valorRecebido;
    private BigDecimal valorTotal;
    private BigDecimal balance;
    private LocalDateTime entryAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;
    private String description;

    private String enderecoEntrega;
    private boolean active;

    public Pedido(){
        items = new ArrayList<>();
    }

    public Pedido(LocalDateTime entryAt){
       this.entryAt = entryAt;
       items = new ArrayList<>();
    }

    public void setActive(boolean active){
        this.active = active;
    }
    public boolean getActive(){
        return active;
    }
}
