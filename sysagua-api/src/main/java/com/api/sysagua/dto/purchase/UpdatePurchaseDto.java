package com.api.sysagua.dto.purchase;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePurchaseDto {

    @Schema(description = "Lista de produtos com seus dados atualizados na compra")
    private List<CreateProductPurchaseDto> productPurchases;

    @Schema(description = "Identificador único do fornecedor associado à compra", example = "2")
    private Long supplierId;

    @Schema(description = "Descrição da compra", example = "Sem descrição")
    private String description;

}
