package com.api.sysagua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    PENDING("Pendente"),
    FINISHED("Finalizado"),
    LATE("Atrasado"),
    CANCELED("Cancelado");

    private final String description;
}
