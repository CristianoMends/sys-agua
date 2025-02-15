package com.api.sysagua.docs;

import com.api.sysagua.dto.transaction.ViewTransactionDto;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "Transaction Controller", description = "Controlador responsável pela gestão das transações financeiras.")
public interface TransactionDoc {


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
    ResponseEntity<List<ViewTransactionDto>> listTransactions(
            Long id,
            BigDecimal amountStart,
            BigDecimal amountEnd,
            TransactionType type,
            String description,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            UUID reposnsibleUserId,
            Long orderId,
            Long purchaseId
    );
}

