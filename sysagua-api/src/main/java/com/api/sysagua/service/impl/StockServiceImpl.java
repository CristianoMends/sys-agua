package com.api.sysagua.service.impl;

import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.SearchStockDto;
import com.api.sysagua.dto.stock.UpdateStockDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Stock;
import com.api.sysagua.repository.ProductPurchaseRepository;
import com.api.sysagua.repository.ProductRepository;
import com.api.sysagua.repository.StockRepository;
import com.api.sysagua.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPurchaseRepository productPurchaseRepository;

    @Override
    public void addProduct(AddProductDto dto) {

        var productPurchase =
                this.productPurchaseRepository
                        .findById(dto.getProductPurchaseId())
                        .orElseThrow(() -> new BusinessException(
                                String.format("productPurchase with id %d Not found",
                                        dto.getProductPurchaseId())
                                ,HttpStatus.NOT_FOUND));


        var stock = this.stockRepository.findProduct(productPurchase.getProduct().getId());

        if (stock.isPresent()){
            stock.get().setQuantity(stock.get().getQuantity() + productPurchase.getQuantity());
            stock.get().setEntries(stock.get().getEntries() + productPurchase.getQuantity());
            stock.get().setCost(productPurchase.getPurchasePrice());
            stock.get().setUpdatedAt(LocalDate.now());
            this.stockRepository.save(stock.get());
        }else{
            var stockToSave = new Stock();
            stockToSave.setProduct(productPurchase.getProduct());
            stockToSave.setQuantity(productPurchase.getQuantity());
            stockToSave.setEntries(productPurchase.getQuantity());
            stockToSave.setPrice(dto.getPrice());
            stockToSave.setCost(productPurchase.getPurchasePrice());
            stockToSave.setExits(0);
            stockToSave.setAddedAt(LocalDate.now());
            this.stockRepository.save(stockToSave);
        }
    }

    @Override
    public List<Stock> getStock(SearchStockDto dto) {

        return this.stockRepository.findByFilters(
                dto.getId(),
                dto.getQuantityStart(),
                dto.getQuantityEnd(),
                dto.getExitsStart(),
                dto.getExitsEnd(),
                dto.getAddedAtStart(),
                dto.getAddedAtEnd(),
                dto.getEntriesStart(),
                dto.getEntriesEnd(),
                dto.getProductId()
        );
    }

    @Override
    public void updateStock(UpdateStockDto dto) {
        var stock = this.stockRepository.findProduct(dto.getProductId()).orElseThrow(
                () -> new BusinessException(String.format("Stock of product with id %d not found", dto.getProductId()),HttpStatus.NOT_FOUND)
        );

        if (dto.getCost() != null) stock.setCost(stock.getCost());
        if (dto.getQuantity() != null) stock.setQuantity(dto.getQuantity());
        if (dto.getPrice() != null) stock.setPrice(dto.getPrice());
        if (dto.getExits() != null) stock.setExits(dto.getExits());
        if (dto.getEntries() != null) stock.setEntries(dto.getEntries());

        stock.setUpdatedAt(LocalDate.now());
        this.stockRepository.save(stock);
    }
}
