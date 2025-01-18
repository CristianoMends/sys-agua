package edu.pies.sysaguaapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private String registeredAt;
    private String updatedAt;
}

