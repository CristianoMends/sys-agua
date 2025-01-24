package com.api.sysagua.service;

import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.SearchStockDto;
import com.api.sysagua.dto.stock.UpdateStockDto;
import com.api.sysagua.model.Stock;

import java.util.List;

public interface StockService {

    void addProduct(AddProductDto dto);

    List<Stock> getStock(SearchStockDto dto);

    void updateStock(Long id,UpdateStockDto dto);

}
