package edu.pies.sysaguaapp.models.Pedido;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.pies.sysaguaapp.models.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemPedido {
    private Long id;
    private Produto product;
    private Integer quantity;
    private BigDecimal purchasePrice;
}
