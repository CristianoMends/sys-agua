package com.api.sysagua.dto;

import com.api.sysagua.enumeration.Access;
import com.api.sysagua.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDto {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 20, message = "Name should not exceed 20 characters")
    private String name;

    @Size(max = 30, message = "Surname should not exceed 30 characters")
    private String surname;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "\\d{10,11}", message = "Phone must contain 10 or 11 digits")
    private String phone;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(
            regexp = "^[A-Za-z]+[.A-Za-z0-9]+[A-Za-z0-9]+@(gmail\\.com|outlook\\.com|hotmail\\.com|yahoo\\.com)$",
            message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 20, message = "Password should have between 6 and 20 characters")
    private String password;

    private Access access;

    public User toModel(){
        return new User(
                getName(),
                getSurname(),
                getPhone(),
                getEmail(),
                getPassword(),
                getAccess()
        );
    }

}