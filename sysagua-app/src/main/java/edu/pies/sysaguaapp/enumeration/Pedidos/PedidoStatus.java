package edu.pies.sysaguaapp.enumeration.Pedidos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PedidoStatus {
    PENDING("Pendente"),
    FINISHED("Concluido"),
    CANCELED("Cancelado");

    private final String descricao;
}
