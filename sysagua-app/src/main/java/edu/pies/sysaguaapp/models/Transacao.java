package edu.pies.sysaguaapp.models;


import edu.pies.sysaguaapp.enumeration.Transacao.TransacaoTipo;
import edu.pies.sysaguaapp.models.Pedido.Pedido;
import edu.pies.sysaguaapp.models.compras.Compra;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Transacao {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private TransacaoTipo type;
    private String description;
    private Pedido order;
    private Compra purchase;
}
