package edu.pies.sysaguaapp.models;

import edu.pies.sysaguaapp.enumeration.Usuarios.UserAccess;
import edu.pies.sysaguaapp.enumeration.Usuarios.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private UserStatus status;
    private UserAccess access;
}
