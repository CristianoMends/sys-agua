package com.api.sysagua.dto.transaction;

import com.api.sysagua.dto.order.ViewOrderDto;
import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ViewOrderDto.class, name = "order"),
        @JsonSubTypes.Type(value = ViewPurchaseDto.class, name = "purchase")
})
public abstract class ViewTransactableDto {

}
