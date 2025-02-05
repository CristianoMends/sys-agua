package edu.pies.sysaguaapp.models.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PedidoStatus {
    PENDING("Pendente"),
    DELIVERED("Entregue"),
    LATE("Atrasado"),
    CANCELED("Cancelado"),
    CONCLUDED("Concluido");

    private final String descricao;
}
