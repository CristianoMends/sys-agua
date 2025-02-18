package com.api.sysagua.dto.purchase;

import com.api.sysagua.dto.transaction.ViewTransactableDto;
import com.api.sysagua.enumeration.DeliveryStatus;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
import com.api.sysagua.model.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para visualização de uma compra")
public class ViewPurchaseDto extends ViewTransactableDto {

    @Schema(description = "Identificador único da compra", example = "1")
    private Long id;

    @Schema(description = "Quantia já paga do valor da compra", example = "249.99")
    private BigDecimal paidAmount;

    @Schema(description = "Quantia total da compra", example = "549.99")
    private BigDecimal totalAmount;

    @Schema(description = "Valor pendente da compra", example = "300.00")
    private BigDecimal balance;

    @Schema(description = "Data e hora da criação da compra", example = "2025-01-14T10:00:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Data e hora da entrada da compra", example = "2025-01-14T10:00:00Z")
    private LocalDateTime entryAt;

    @Schema(description = "Data e hora da cancelamento da compra", example = "2025-01-14T10:00:00Z")
    private LocalDateTime canceledAt;

    @Schema(description = "Data e hora da finalização da compra", example = "2025-01-14T10:00:00Z")
    private LocalDateTime finishedAt;

    @Schema(description = "metodo de pagamento", example = "PIX")
    private PaymentMethod paymentMethod;

    @Schema(description = "Status do pagamento da compra", example = "PAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Número nota fiscal eletrônica", example = "123456789")
    private String nfe;

    @Schema(description = "Descrição da compra", example = "Sem descrição")
    private String description;

    @Schema(description = "Se a compra está ativa no sistema", example = "2025-01-14T10:00:00Z")
    private Boolean active;

    @Schema(description = "Lista de itens comprados com os detalhes do produto",
            implementation = ViewProductPurchaseDto.class)
    private List<ViewProductPurchaseDto> items;

    @Schema(description = "Fornecedor da compra", implementation = Supplier.class)
    private Supplier supplier;


    public ViewPurchaseDto(Long id, BigDecimal totalAmount, BigDecimal balance, LocalDateTime createdAt, LocalDateTime canceledAt, LocalDateTime finishedAt, PaymentMethod paymentMethod, PaymentStatus paymentStatus, String nfe, String description, Boolean active, List<ViewProductPurchaseDto> list, Supplier supplier) {
        super();
        this.id = id;
        this.totalAmount = totalAmount;
        this.balance = balance;
        this.createdAt = createdAt;
        this.canceledAt = canceledAt;
        this.finishedAt = finishedAt;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.nfe = nfe;
        this.description = description;
        this.active = active;
        this.supplier = supplier;
    }
}
