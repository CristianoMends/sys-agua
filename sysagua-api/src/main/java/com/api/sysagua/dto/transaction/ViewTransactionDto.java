package com.api.sysagua.dto.transaction;

import com.api.sysagua.dto.order.ViewOrderDto;
import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import com.api.sysagua.dto.user.ViewUserDto;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Schema(description = "Data e hora em que a transação foi criada", example = "2024-02-04T12:30:45")
    private LocalDateTime createdAt;

    @Schema(description = "Valor da transação", example = "250.75")
    private BigDecimal amount;

    @Schema(description = "Tipo da transação", example = "INCOME")
    private TransactionType type;

    @Schema(description = "Metodo de pagamento")
    private PaymentMethod paymentMethod;

    @Schema(description = "Descrição da transação", example = "Pagamento de Pedido")
    private String description;

    @Schema(description = "Usuário reponsavel pela transação")
    private ViewUserDto responsibleUser;

    @Schema(description = "Transação associada (pode ser um pedido ou uma compra)")
    @JsonProperty("transactable")
    private ViewTransactableDto transactable;
}
