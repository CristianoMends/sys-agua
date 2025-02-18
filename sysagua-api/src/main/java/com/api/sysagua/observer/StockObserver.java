package com.api.sysagua.observer;

import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.model.Stock;

public interface StockObserver {
    void update(Stock stock, MovementType type, Integer quantity, String description);
}
