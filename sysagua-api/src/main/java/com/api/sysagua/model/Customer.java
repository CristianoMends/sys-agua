package com.api.sysagua.model;

import com.api.sysagua.dto.customer.AddressDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    @Embedded
    private AddressDto address;
    private String phone;
    private LocalDate createdAt;
    private Boolean active;

    public Customer(String name, AddressDto address,String phone){
        setName(name);
        setAddress(address);
        setPhone(phone);
    }

}
