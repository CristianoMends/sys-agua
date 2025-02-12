package edu.pies.sysaguaapp.models;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {
    private Long id;
    private String socialReason;
    private String cnpj;
    private Address address;
    private String phone;
    private String tradeName;
    private String stateRegistration;
    private String municipalRegistration;
    private Boolean active;

    public boolean isActive() {return active;}
}
