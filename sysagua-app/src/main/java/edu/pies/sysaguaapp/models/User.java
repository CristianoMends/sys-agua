package edu.pies.sysaguaapp.models;

import edu.pies.sysaguaapp.enumeration.UserAccess;
import edu.pies.sysaguaapp.enumeration.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;
    private UserStatus status;
    private UserAccess access;
}
