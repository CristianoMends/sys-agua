package com.api.sysagua.observer;

import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Purchase;
import com.api.sysagua.model.Stock;
import com.api.sysagua.model.Transactable;

import java.math.BigDecimal;

public interface TransactionObserver {
    void update(Transactable purchase, BigDecimal amount, PaymentMethod paymentMethod, String description);
}
