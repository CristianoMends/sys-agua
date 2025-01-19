package edu.pies.sysaguaapp.models;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal price;
    private BigDecimal cost;
    private String brand;
    private String category;
    private String line;
    private String ncm;
    private String createdat;
    private String updatedat;
    private boolean active;
}

