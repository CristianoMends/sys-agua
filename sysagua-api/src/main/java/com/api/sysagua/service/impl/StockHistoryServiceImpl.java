package com.api.sysagua.service.impl;

import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.factory.StockHistoryFactory;
import com.api.sysagua.model.Stock;
import com.api.sysagua.observer.StockObserver;
import com.api.sysagua.repository.StockHistoryRepository;
import com.api.sysagua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockHistoryServiceImpl implements StockObserver {

    @Autowired
    private UserService userService;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Override
    public void update(Stock stock, MovementType type, Integer quantity, String description) {
        var user = this.userService.getLoggedUser();
        var history = StockHistoryFactory.createStockHistory(user, stock, type, quantity, description);
        this.stockHistoryRepository.save(history);
    }
}
