package com.api.sysagua.controller;

import com.api.sysagua.docs.StockHistoryDoc;
import com.api.sysagua.dto.stockHistory.ViewStockHistoryDto;
import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.model.StockHistory;
import com.api.sysagua.repository.StockHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("stockHistory")
public class StockHistoryController implements StockHistoryDoc {
    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @GetMapping
    public ResponseEntity<List<ViewStockHistoryDto>> list(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) MovementType type,
            @RequestParam(required = false) Integer quantityStart,
            @RequestParam(required = false) Integer quantityEnd,
            @RequestParam(required = false) LocalDateTime dateStart,
            @RequestParam(required = false) LocalDateTime dateEnd,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) UUID responsibleUserId,
            @RequestParam(required = false) Long stockId,
            @RequestParam(required = false) Long productId
    ) {
        List<StockHistory> list = this.stockHistoryRepository.list(
                id,
                type,
                quantityStart,
                quantityEnd,
                dateStart,
                dateEnd,
                description,
                responsibleUserId,
                stockId,
                productId
        );

        return ResponseEntity.ok(list.stream().map(StockHistory::toView).toList());
    }

}
