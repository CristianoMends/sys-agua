package edu.pies.sysaguaapp.enumeration.Usuarios;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAccess {
    DEVELOPER("Desenvolvedor"),
    OWNER("Dono"),
    EMPLOYEE("Funcionario"),
    MANAGER("Gestor");

    private final String description;

    }
