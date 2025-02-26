package com.api.sysagua.observer;

import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.model.Transactable;

import java.math.BigDecimal;

public interface TransactionObserver {
    void update(Transactable purchase, BigDecimal amount, PaymentMethod paymentMethod, String description, TransactionType transactionType);
}
