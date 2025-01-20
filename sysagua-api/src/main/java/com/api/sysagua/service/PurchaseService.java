package com.api.sysagua.service;

import com.api.sysagua.dto.purchase.CreatePurchaseDto;
import com.api.sysagua.dto.purchase.SearchPurchaseDto;
import com.api.sysagua.dto.purchase.UpdatePurchaseDto;
import com.api.sysagua.dto.purchase.ViewPurchaseDto;

import java.util.List;

public interface PurchaseService {

    void create(CreatePurchaseDto dto);

    List<ViewPurchaseDto> list(SearchPurchaseDto searchDto);

    void update(Long id, UpdatePurchaseDto dto);

    void delete(Long id);

}
