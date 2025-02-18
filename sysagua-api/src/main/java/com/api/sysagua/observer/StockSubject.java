package com.api.sysagua.observer;

import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.model.Stock;

public interface StockSubject {
    void addObserver(StockObserver observer);
    void removeObserver(StockObserver observer);
    void notifyObservers(Stock stock, MovementType type, Integer quantity, String description);
}
