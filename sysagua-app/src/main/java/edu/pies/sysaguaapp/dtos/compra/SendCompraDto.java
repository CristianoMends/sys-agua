package edu.pies.sysaguaapp.dtos.compra;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.pies.sysaguaapp.models.compras.ItemCompra;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SendCompraDto {
    private List<ItemCompraDto> items;
    private Long supplierId;

    public SendCompraDto() {
        items = new ArrayList<>();
    }

    public SendCompraDto(List<ItemCompraDto> items, Long supplierId) {
        this.items = items;
        this.supplierId = supplierId;
    }

    public void addItem(ItemCompraDto item){
        items.add(item);
    }

}
