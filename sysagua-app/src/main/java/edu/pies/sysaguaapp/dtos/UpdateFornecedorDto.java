package edu.pies.sysaguaapp.dtos;

import edu.pies.sysaguaapp.models.Fornecedor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateFornecedorDto {
    private String socialReason;
    private String cnpj;
    private String phone;
    private String number;
    private String street;
    private String neighborhood;
    private String city;
    private String state;

    public UpdateFornecedorDto() {}

    public UpdateFornecedorDto(Fornecedor fornecedor) {
        this.socialReason = fornecedor.getSocialReason();
        this.cnpj = fornecedor.getCnpj();
        this.phone = fornecedor.getPhone();

        if (fornecedor.getAddress() != null) {
            this.number = fornecedor.getAddress().getNumber();
            this.street = fornecedor.getAddress().getStreet();
            this.neighborhood = fornecedor.getAddress().getNeighborhood();
            this.city = fornecedor.getAddress().getCity();
            this.state = fornecedor.getAddress().getState();
        }
    }
}
