package com.api.sysagua.factory;

import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.model.Stock;
import com.api.sysagua.model.StockHistory;
import com.api.sysagua.model.User;

public class StockHistoryFactory {

    public static StockHistory createStockHistory(User user, Stock stock, MovementType type, int quantity, String description) {
        return new StockHistory(user, stock, type, quantity, description);
    }

}
