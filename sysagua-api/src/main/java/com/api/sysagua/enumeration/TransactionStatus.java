package com.api.sysagua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionStatus {

    PAID("Paga"),
    PENDING("Pendente"),
    CANCELED("Cancelada");

    private final String description;
}
