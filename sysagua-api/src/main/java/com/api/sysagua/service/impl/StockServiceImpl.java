package com.api.sysagua.service.impl;

import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.SearchStockDto;
import com.api.sysagua.dto.stock.UpdateStockDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Stock;
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

    @Override
    public void addProduct(AddProductDto dto) {

        //busca um produto com o id informado
        var product = this.productRepository
                        .findById(dto.getProductId())
                        //lança exeção se o produto não foi encontrado
                        .orElseThrow(() -> new BusinessException(
                                String.format("Product with id %d Not found",dto.getProductId())
                                ,HttpStatus.NOT_FOUND));

        if (!product.getActive()){//se o produto não está ativo, não é possivel adicionar ao estoque
            throw new BusinessException(String.format("Product with id %d is not active",dto.getProductId()));
        }

        var stock = this.stockRepository.findProduct(product.getId());

        if (stock.isPresent()) {
            stock.get().setQuantity(stock.get().getQuantity() + dto.getQuantity());
            stock.get().setEntries(stock.get().getEntries() + dto.getQuantity());
            stock.get().setCost(dto.getCost());
            stock.get().setUpdatedAt(LocalDate.now());
            this.stockRepository.save(stock.get());
        } else {
            var stockToSave = new Stock();
            stockToSave.setProduct(product);
            stockToSave.setQuantity(dto.getQuantity());
            stockToSave.setEntries(dto.getQuantity());
            stockToSave.setPrice(dto.getPrice());
            stockToSave.setCost(dto.getCost());
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
                () -> new BusinessException(String.format("Stock of product with id %d not found", dto.getProductId()), HttpStatus.NOT_FOUND)
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
