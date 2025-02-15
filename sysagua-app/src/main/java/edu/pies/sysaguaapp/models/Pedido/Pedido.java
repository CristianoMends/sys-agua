package edu.pies.sysaguaapp.models.Pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatus;
import edu.pies.sysaguaapp.enumeration.Pedidos.PedidoStatusPagamento;
import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;
import edu.pies.sysaguaapp.models.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Pedido {
    private Long id;
    private LocalDateTime dataPedido;
    private Clientes cliente;
    private Entregador entregador;
    private List<Produto> produtos;
    private PedidoStatus status;
    private PedidoStatusPagamento statusPagamento;
    private BigDecimal valorRecebido;
    private BigDecimal valorTotal;
    private PaymentMethod tipoPagamento;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime canceledAt;

    private String enderecoEntrega;
    private boolean active;

    public Pedido(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    public void setActive(boolean active){
        this.active = active;
    }
    public boolean getActive(){
        return active;
    }
}
