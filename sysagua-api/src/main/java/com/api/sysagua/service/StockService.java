package com.api.sysagua.service;

import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.UpdateStockDto;
import com.api.sysagua.model.Stock;

import java.time.LocalDateTime;
import java.util.List;

public interface StockService {

    void addProduct(AddProductDto dto);

    List<Stock> getStock(Long id,
                         Integer initialQuantityStart,
                         Integer initialQuantityEnd,
                         Integer totalEntriesStart,
                         Integer totalEntriesEnd,
                         Integer totalWithdrawalsStart,
                         Integer totalWithdrawalsEnd,
                         LocalDateTime createdAtStart,
                         LocalDateTime createdAtEnd,
                         LocalDateTime updatedAtStart,
                         LocalDateTime updatedAtEnd,
                         Long productId,
                         String productName);

    void updateStock(Long id, UpdateStockDto dto);

}
