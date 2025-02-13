package com.api.sysagua.docs;

import com.api.sysagua.dto.cashier.ViewCashierDto;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface CashierDoc {

    @Operation(
            summary = "Adiciona saldo ao caixa",
            description = "Aumenta o saldo do caixa com o valor informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "sucesso.",content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @PostMapping("{balance}")
    ResponseEntity<Void> addBalance(
            @Parameter(description = "Valor a ser adicionado ao caixa", example = "100.00")
            @PathVariable BigDecimal balance
    );

    @Operation(
            summary = "Lista o caixa e transações",
            description = "Recupera os detalhes do caixa, incluindo transações, com base em filtros opcionais."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    @GetMapping
    ResponseEntity<ViewCashierDto> list(
            @Parameter(description = "ID da transação") Long transactionId,
            @Parameter(description = "Status da transação") TransactionStatus transactionStatus,
            @Parameter(description = "Valor mínimo da transação", example = "50.00") BigDecimal amountStart,
            @Parameter(description = "Valor máximo da transação", example = "500.00") BigDecimal amountEnd,
            @Parameter(description = "Tipo da transação") TransactionType type,
            @Parameter(description = "Descrição da transação", example = "Pagamento recebido") String description,
            @Parameter(description = "Data de criação inicial", example = "2024-02-01T00:00:00") LocalDateTime createdAtStart,
            @Parameter(description = "Data de criação final", example = "2024-02-12T23:59:59") LocalDateTime createdAtEnd,
            @Parameter(description = "ID do usuario responsavel pela transação") UUID responsibleUserId,
            @Parameter(description = "ID do pedido associado") Long orderId,
            @Parameter(description = "ID da compra associada") Long purchaseId
    );
}
