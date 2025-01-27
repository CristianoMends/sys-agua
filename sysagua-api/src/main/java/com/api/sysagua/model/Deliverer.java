package com.api.sysagua.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Deliverer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliverer_id", nullable = false)
    private Long id;
    private String name;
    @Embedded
    private String phone;
    private Boolean active;
    private LocalDate createdAt;

    public Deliverer(String name, String phone){
        setName(name);
        setPhone(phone);

    }

}
