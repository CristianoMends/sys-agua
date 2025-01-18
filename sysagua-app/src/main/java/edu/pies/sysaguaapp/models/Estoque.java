package edu.pies.sysaguaapp.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {
    private Long id;
    private Produto produto;
    private BigDecimal cost;
    private int quantity;

}
