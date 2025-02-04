package com.api.sysagua.service;

import com.api.sysagua.dto.purchase.CreatePurchaseDto;
import com.api.sysagua.dto.purchase.UpdatePurchaseDto;
import com.api.sysagua.dto.purchase.ViewPurchaseDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseService {

    void create(CreatePurchaseDto dto);

    List<ViewPurchaseDto> list(
            Long id,
            BigDecimal totalValueStart,
            BigDecimal totalValueEnd,
            Boolean active,
            LocalDateTime updatedAtStart,
            LocalDateTime updatedAtEnd,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            Long supplierId,
            Long productId,
            LocalDateTime finishedAtStart,
            LocalDateTime finishedAtEnd,
            LocalDateTime canceledAtStart,
            LocalDateTime canceledAtEnd,
            String description
    );

    void update(Long id, UpdatePurchaseDto dto);

    void delete(Long id);

}
