package edu.pies.sysaguaapp.dtos.compra;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.pies.sysaguaapp.enumeration.PaymentMethod;
import edu.pies.sysaguaapp.enumeration.PaymentStatus;
import edu.pies.sysaguaapp.models.compras.ItemCompra;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class SendCompraDto {
    private List<ItemCompraDto> items;
    private Long supplierId;
    private BigDecimal paidAmount;
    private BigDecimal totalAmount;
    private LocalDateTime entryAt;
    private PaymentMethod paymentMethod;
    private String nfe;
    private String description;

    public SendCompraDto() {
        items = new ArrayList<>();
    }

    public SendCompraDto(List<ItemCompraDto> items, Long supplierId) {
        this.items = items;
        this.supplierId = supplierId;
    }

}
