package edu.pies.sysaguaapp.models.Pedido;

import edu.pies.sysaguaapp.models.Clientes;
import edu.pies.sysaguaapp.models.Entregador;
import edu.pies.sysaguaapp.models.MetodoPagamento;
import edu.pies.sysaguaapp.models.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private Long id;
    private Long numeroPedido;
    private Clientes cliente;
    private Entregador entregador;
    private List<Produto> produtos;
    private PedidoStatus status;

    private LocalDateTime dataPedido;

    private BigDecimal valorRecebido;
    private BigDecimal valorTotal;

    private MetodoPagamento tipoPagamento;
    private PedidoStatusPagamento statusPagamento;

    private boolean active;

}
