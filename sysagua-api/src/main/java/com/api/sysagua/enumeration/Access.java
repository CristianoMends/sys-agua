package com.api.sysagua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Access {
    OWNER("Dono"),
    EMPLOYEE("Funcionario"),
    MANAGER("Gestor");

    private final String description;

    }
