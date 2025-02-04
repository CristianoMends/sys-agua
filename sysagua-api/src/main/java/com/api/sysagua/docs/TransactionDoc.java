package com.api.sysagua.docs;

import com.api.sysagua.model.Transaction;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.StatusTransaction;
import com.api.sysagua.enumeration.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Transaction Controller", description = "Controlador responsável pela gestão das transações financeiras.")
public interface TransactionDoc {


    @Operation(
            summary = "Cancelamento de uma transação",
            description = "Este método cancela uma transação com base no ID fornecido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transação cancelada com sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> cancelTransaction(Long id);

    @Operation(
            summary = "Finalização de uma transação",
            description = "Este método finaliza uma transação com base no ID fornecido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transação finalizada com sucesso.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<Void> finishTransaction(Long id);

    @Operation(
            summary = "Busca de transações por filtros",
            description = "Este método permite buscar transações com base em vários filtros, como valor, tipo, status, forma de pagamento e datas de criação e conclusão."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transações listadas com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.", content = @Content()),
            @ApiResponse(responseCode = "403", description = "Acesso não autorizado.", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.", content = @Content())
    })
    ResponseEntity<List<Transaction>> listTransactions(
            Long id,
            StatusTransaction status,
            BigDecimal amountStart,
            BigDecimal amountEnd,
            TransactionType type,
            PaymentMethod paymentMethod,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime finishedAtStart,
            LocalDateTime finishedAtEnd,
            LocalDateTime canceledAtStart,
            LocalDateTime canceledAtEnd
    );
}

