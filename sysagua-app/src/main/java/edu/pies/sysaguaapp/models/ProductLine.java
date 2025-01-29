package edu.pies.sysaguaapp.models;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductLine {
    private Long id;
    private String name;
    private Boolean active;

    public ProductLine(Long id) {
        this.id = id;
    }
}
