package edu.pies.sysaguaapp.enumeration.Pedidos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PedidoStatusPagamento {
    PAID("Pago"),
    CANCELED("Cancelado"),
    PENDING("Pendente");

    private final String descricao;
}
