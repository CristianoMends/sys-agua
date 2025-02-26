package edu.pies.sysaguaapp.dtos.pedido;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ItemPedidoDto {
    private Long productId;
    private int quantity;
    private BigDecimal purchasePrice;
}
