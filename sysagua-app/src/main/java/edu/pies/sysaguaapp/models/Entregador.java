package edu.pies.sysaguaapp.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entregador {
    private Long id;
    private String name;
    private String phone;
    private Boolean active;
    private String createdAt;

    public boolean isActive() {
        return active;
    }
}
