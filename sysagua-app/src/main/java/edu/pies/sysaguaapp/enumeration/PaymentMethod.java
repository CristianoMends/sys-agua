package edu.pies.sysaguaapp.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMethod {

    PIX("Pix"),
    DEBIT("Débito"),
    CREDIT("Crédito"),
    MONEY("Dinheiro"),
    UNDEFINED("Indefinido"),
    TICKET("Boleto");

    private final String description;
}
