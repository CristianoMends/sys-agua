package com.api.sysagua.factory;

import com.api.sysagua.dto.order.CreateOrderDto;
import com.api.sysagua.model.Order;

public interface OrderFactory {
    Order createOrder(CreateOrderDto dto);
}

