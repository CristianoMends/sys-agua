package edu.pies.sysaguaapp.models.produto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    private Long id;
    private String name;
    private Boolean active;

    public ProductCategory(Long id) {
        this.id = id;
    }
}
