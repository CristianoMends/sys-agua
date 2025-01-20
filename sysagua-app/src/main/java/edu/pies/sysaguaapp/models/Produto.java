package edu.pies.sysaguaapp.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private String createdAt;
    private String updatedAt;
    private Boolean active;
}

