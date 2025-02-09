package edu.pies.sysaguaapp.models.compras;

import edu.pies.sysaguaapp.models.Fornecedor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class Compra {
    private Long id;
    private BigDecimal totalValue;
    private Boolean active;
    private String updatedAt;
    private String createdAt;
    private List<ItemCompra> items;
    private Fornecedor supplier;
    //metodos de pagamento
    //data entrada da nota
    //data de pagamento
    //data cancelamento
    //descrição
    //nfe
    //status de pagamento

    public Compra() {
        items = new ArrayList<>();
    }

    public Compra(String createdAt) {
        this.createdAt = createdAt;
        items = new ArrayList<>();
    }
}
