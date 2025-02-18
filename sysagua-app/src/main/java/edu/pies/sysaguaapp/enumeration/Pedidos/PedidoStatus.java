package edu.pies.sysaguaapp.enumeration.Pedidos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PedidoStatus {
    PENDING("Pendente"),
    FINISHED("Finalizado"),
    CANCELED("Cancelado");

    private final String description;
}
