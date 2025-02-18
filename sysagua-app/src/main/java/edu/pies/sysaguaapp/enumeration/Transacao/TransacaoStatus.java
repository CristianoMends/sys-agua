package edu.pies.sysaguaapp.enumeration.Transacao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransacaoStatus {

    PAID("Paga"),
    PENDING("Pendente"),
    CANCELED("Cancelada");

    private final String description;
}
