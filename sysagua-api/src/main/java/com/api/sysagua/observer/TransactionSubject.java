package com.api.sysagua.observer;

import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.model.Transactable;

import java.math.BigDecimal;

public interface TransactionSubject {
    void addObserver(TransactionObserver observer);
    void removeObserver(TransactionObserver observer);
    void notifyObservers(Transactable purchase, BigDecimal amount, PaymentMethod paymentMethod, String description);
}
