package com.api.sysagua.service.impl;

import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.stock.UpdateStockDto;
import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Product;
import com.api.sysagua.model.Stock;
import com.api.sysagua.observer.StockObserver;
import com.api.sysagua.observer.StockSubject;
import com.api.sysagua.repository.ProductRepository;
import com.api.sysagua.repository.StockRepository;
import com.api.sysagua.service.StockService;
import com.api.sysagua.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl implements StockService, StockSubject {
    private final List<StockObserver> observers = new ArrayList<>();

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StockObserver stockObserver;

    @PostConstruct
    public void init() {
        addObserver(stockObserver);
    }

    @Override
    public void addObserver(StockObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(StockObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(Stock stock, MovementType type, Integer quantity, String description) {
        for (var o : observers) {
            o.update(stock, type, quantity, description);
        }
    }

    @Override
    public void addProduct(AddProductDto dto) {

        var product = this.productRepository
                .findById(dto.getProductId())
                .orElseThrow(() -> new BusinessException(
                        String.format("Product with id %d Not found", dto.getProductId())
                        , HttpStatus.NOT_FOUND));

        if (!product.getActive()) {//se o produto não está ativo, não é possivel adicionar ao estoque
            throw new BusinessException(String.format("Product with id %d is not active", dto.getProductId()));
        }

        var stock = this.stockRepository.findProduct(product.getId());

        if (stock.isPresent()) {
            stock.get().setTotalEntries(stock.get().getTotalEntries() + dto.getQuantity());
            stock.get().setUpdatedAt(getCurrentTimestamp());
            var saved = this.stockRepository.save(stock.get());

            notifyObservers(saved, MovementType.ENTRY, dto.getQuantity(), "Entrada de produto");
            return;
        }

        var newStock = new Stock();
        newStock.setProduct(product);
        newStock.setInitialQuantity(dto.getQuantity());
        newStock.setTotalEntries(0);
        newStock.setTotalWithdrawals(0);
        newStock.setCreatedAt(getCurrentTimestamp());
        var saved = this.stockRepository.save(newStock);
        notifyObservers(saved, MovementType.ENTRY, dto.getQuantity(), "Criação de estoque");
    }
    @Override
    public void addWithdraw(int quantity, Product product) {
        var stock = this.stockRepository.findProduct(product.getId()).orElseThrow(
                () -> new BusinessException("Stock by product not found", HttpStatus.NOT_FOUND)
        );
        stock.setTotalWithdrawals(stock.getTotalWithdrawals() + quantity);
        var saved = this.stockRepository.save(stock);
        notifyObservers(saved, MovementType.WITHDRAWAL, quantity, "Saída por venda");
    }

    @Override
    public void processStockReturn(Long productId, int quantity, String reason) {
        var stock = this.stockRepository.findProduct(productId);

        if (stock.isPresent()) {
            var productStock = stock.get();
            int newTotalEntries = productStock.getTotalEntries() + quantity; // quantity pode ser positivo ou negativo

            if (newTotalEntries < 0) {
                throw new IllegalArgumentException("Não é possível ter estoque negativo.");
            }

            productStock.setTotalEntries(newTotalEntries);
            productStock.setUpdatedAt(getCurrentTimestamp());

            var saved = this.stockRepository.save(productStock);

            // Determinar se é entrada ou saída para a notificação
            MovementType movementType = (quantity > 0) ? MovementType.ENTRY : MovementType.WITHDRAWAL;
            notifyObservers(saved, movementType, Math.abs(quantity), reason);
        }
    }


    @Override
    public List<Stock> getStock(Long id,
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
                                String productName) {

        if (productName == null) productName = "";
        return this.stockRepository.list(
                id,
                initialQuantityStart,
                initialQuantityEnd,
                totalEntriesStart,
                totalEntriesEnd,
                totalWithdrawalsStart,
                totalWithdrawalsEnd,
                createdAtStart,
                createdAtEnd,
                updatedAtStart,
                updatedAtEnd,
                productId,
                productName
        );
    }


    @Override
    public void updateStock(Long id, UpdateStockDto dto) {
        var stock = this.stockRepository.findById(id).orElseThrow(
                () -> new BusinessException(String.format("Stock with id %d not found", id), HttpStatus.NOT_FOUND)
        );

        if (dto.getProductId() != null) {
            var product = this.productRepository.findById(dto.getProductId()).orElseThrow(
                    () -> new BusinessException("Product not found", HttpStatus.NOT_FOUND)
            );

            if (!product.getActive()) {//se o produto não está ativo, não é possivel adicionar ao estoque
                throw new BusinessException(String.format("Product with id %d is not active", dto.getProductId()));
            }

            stock.setProduct(product);
        }
        if (dto.getQuantity() != null) {
            if (stock.getTotalEntries() > 0) {
                stock.setCurrentQuantity(dto.getQuantity());
            }
        }

        stock.setUpdatedAt(LocalDateTime.now());
        var saved = this.stockRepository.save(stock);

        notifyObservers(saved, MovementType.ENTRY, dto.getQuantity(), "Atualização de estoque");
    }

    private LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }
}
