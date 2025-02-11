package edu.pies.sysaguaapp.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    PAID("Pago"),
    CANCELED("Cancelado"),
    PENDING("Pendente");

    private final String description;

}
