package com.api.sysagua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    PENDING("Pendente"),
    DELIVERED("Entregue"),
    LATE("Atrasado"),
    CANCELED("Cancelado");

    private final String description;
}
