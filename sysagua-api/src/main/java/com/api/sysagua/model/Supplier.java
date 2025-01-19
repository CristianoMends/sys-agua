package com.api.sysagua.model;

import com.api.sysagua.dto.customer.AddressDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String socialReason;
    private String cnpj;
    @Embedded
    private AddressDto address;
    private String phone;
    private Boolean active;

    public Supplier(String socialReason, String cnpj, AddressDto address, String phone) {
        this.socialReason = socialReason;
        this.cnpj = cnpj;
        this.address = address;
        this.phone = phone;
    }
}
