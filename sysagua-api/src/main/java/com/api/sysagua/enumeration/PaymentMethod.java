package com.api.sysagua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMethod {

    PIX("Pix"),
    DEBIT("Débito"),
    CREDIT("Crédito"),
    MONEY("Dinheiro"),
    TICKET("Boleto");

    private final String description;
}
