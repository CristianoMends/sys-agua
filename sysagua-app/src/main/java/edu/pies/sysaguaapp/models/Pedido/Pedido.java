package edu.pies.sysaguaapp.models;

import java.math.BigDecimal;
import java.util.List;

public class Pedido {
    public enum PedidoStatus{

    }
    private Long id;
    private Clientes cliente;
    private Entregador entregador;
    private List<Produto> produtos;
    private PedidoStatus status;

    private BigDecimal total;

    private BigDecimal valorRecebido;
    private BigDecimal valorTotal;

}
