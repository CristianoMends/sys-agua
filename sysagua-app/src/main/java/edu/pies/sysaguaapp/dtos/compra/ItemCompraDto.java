package edu.pies.sysaguaapp.dtos.compra;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ItemCompraDto {
    private Long productId;
    private int quantity;
    private BigDecimal purchasePrice;
}
