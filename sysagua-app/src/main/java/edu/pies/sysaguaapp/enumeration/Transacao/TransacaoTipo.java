package edu.pies.sysaguaapp.enumeration.Transacao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransacaoTipo {

    INCOME("Renda"),
    EXPENSE("Despesa");

    private final String description;
}
