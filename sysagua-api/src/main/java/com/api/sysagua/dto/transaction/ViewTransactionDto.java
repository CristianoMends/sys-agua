package com.api.sysagua.dto.transaction;

import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewTransactionDto {
    @Schema(description = "Identificador único da transação", example = "1")
    private Long id;

    @Schema(description = "Status da transação", example = "COMPLETED")
    private TransactionStatus status;

    @Schema(description = "Data e hora em que a transação foi criada", example = "2024-02-04T12:30:45")
    private LocalDateTime createdAt;

    @Schema(description = "Data e hora de finalização da transação, se aplicável", example = "2024-02-05T14:20:10")
    private LocalDateTime finishedAt;

    @Schema(description = "Data e hora do cancelamento da transação, se aplicável", example = "null")
    private LocalDateTime canceledAt;

    @Schema(description = "Valor da transação", example = "250.75")
    private BigDecimal amount;

    @Schema(description = "Tipo da transação", example = "PAYMENT")
    private TransactionType type;

    @Schema(description = "Método de pagamento utilizado", example = "CREDIT_CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "Descrição da transação", example = "Pagamento de compra")
    private String description;

    @Schema(description = "ID do pedido associado à transação, se aplicável", example = "123")
    private Long orderId;

    @Schema(description = "ID da compra associada à transação, se aplicável", example = "456")
    private Long purchaseId;
}
