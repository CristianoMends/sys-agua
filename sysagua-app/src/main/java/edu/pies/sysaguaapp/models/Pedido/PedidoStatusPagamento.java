package edu.pies.sysaguaapp.models.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PedidoStatusPagamento {
    PENDING("Pendente"),
    CONCLUDED("Concluido"),
    LATE("Atrasado"),
    CANCELED("Cancelado"),
    WAITING("Aguardando");

    private final String descricao;
}
