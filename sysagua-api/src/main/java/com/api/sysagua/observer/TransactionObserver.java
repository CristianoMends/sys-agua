package com.api.sysagua.observer;

import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Purchase;
import com.api.sysagua.model.Stock;

import java.math.BigDecimal;

public interface TransactionObserver {
    void update(Purchase purchase, BigDecimal amount, PaymentMethod paymentMethod, String description);
}
