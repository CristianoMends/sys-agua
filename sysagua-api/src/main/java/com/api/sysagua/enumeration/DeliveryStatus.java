package com.api.sysagua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    PENDING("Pendente"),
    FINISHED("Finalizado"),
    CANCELED("Cancelado");

    private final String description;
}
