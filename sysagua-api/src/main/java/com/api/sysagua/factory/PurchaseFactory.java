package com.api.sysagua.factory;

import com.api.sysagua.dto.purchase.CreatePurchaseDto;
import com.api.sysagua.model.Purchase;

public interface PurchaseFactory {
    Purchase createPurchase(CreatePurchaseDto dto);
}

