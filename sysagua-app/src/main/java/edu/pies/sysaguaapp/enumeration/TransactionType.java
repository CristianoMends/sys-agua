package edu.pies.sysaguaapp.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

    INCOME("Receita"),
    EXPENSE("Despesa");

    private final String description;
}