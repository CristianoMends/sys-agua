package edu.pies.sysaguaapp.models.compras;

import edu.pies.sysaguaapp.models.Produto;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCompra {
    private Long id;
    private Produto product;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private BigDecimal total;

}
