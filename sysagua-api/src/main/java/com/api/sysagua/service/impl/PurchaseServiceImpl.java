package com.api.sysagua.service.impl;

import com.api.sysagua.dto.purchase.*;
import com.api.sysagua.enumeration.TransactionStatus;
import com.api.sysagua.enumeration.TransactionType;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.*;
import com.api.sysagua.repository.*;
import com.api.sysagua.service.PurchaseService;
import com.api.sysagua.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private TransactionService transactionService;

    @Override
    public void create(CreatePurchaseDto dto) {

        var purchase = new Purchase();
        var productPurchases = createProductPurchase(dto.getItems(), purchase);
        var supplier = this.findSupplierById(dto.getSupplierId());
        if (supplier.getActive().equals(false)) {
            throw new BusinessException("Supplier is inactive");
        }

        purchase.setProductPurchases(productPurchases);
        purchase.setSupplier(supplier);
        purchase.setActive(true);
        purchase.setDescription(dto.getDescription());
        purchase.setPaymentMethod(dto.getPaymentMethod());
        var saved = this.purchaseRepository.save(purchase);

        saveOnTransactionHistory(saved);
    }

    private void saveOnTransactionHistory(Purchase purchase){

    }

    @Override
    public void update(Long id, UpdatePurchaseDto dto) {
        Purchase purchase = findPurchaseById(id);

        if (dto.getSupplierId() != null) {
            var supplier = findSupplierById(dto.getSupplierId());
            purchase.setSupplier(supplier);
        }

        if (dto.getProductPurchases() != null) {
            var productPurchases = updateProductPurchase(dto.getProductPurchases(), purchase);
            purchase.setProductPurchases(productPurchases);
        }

        if (dto.getDescription() != null) purchase.setDescription(dto.getDescription());
        if (dto.getPaymentMethod() != null) purchase.setPaymentMethod(dto.getPaymentMethod());

        purchase.setActive(true);
        purchase.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime());
        this.purchaseRepository.save(purchase);
    }

    private List<ProductPurchase> updateProductPurchase(List<CreateProductPurchaseDto> dto, Purchase purchase) {
        List<ProductPurchase> productPurchases = purchase.getProductPurchases();
        productPurchases.clear();
        for (var item : dto) {
            var product = this.findProductById(item.getProductId());
            if (product.getActive().equals(false)) {
                throw new BusinessException(String.format("Product with id %d is inactive", product.getId()));
            }
            productPurchases.add(new ProductPurchase(purchase, product, item.getQuantity(), item.getPurchasePrice()));
        }
        return productPurchases;
    }

    private List<ProductPurchase> createProductPurchase(List<CreateProductPurchaseDto> dto, Purchase purchase) {
        List<ProductPurchase> productPurchases = new ArrayList<>();
        for (var item : dto) {
            var product = this.findProductById(item.getProductId());
            productPurchases.add(new ProductPurchase(purchase, product, item.getQuantity(), item.getPurchasePrice()));
        }
        return productPurchases;
    }

    @Override
    public void delete(Long id) {
        Purchase purchase = findPurchaseById(id);
        purchase.setActive(false);
        purchase.setUpdatedAt(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime());
        this.purchaseRepository.save(purchase);
    }

    @Override
    public List<ViewPurchaseDto> list(
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
    ) {
        return purchaseRepository.list(
                id,
                totalValueStart,
                totalValueEnd,
                active,
                updatedAtStart,
                updatedAtEnd,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd,
                canceledAtStart,
                canceledAtEnd,
                description,
                supplierId,
                productId
        ).stream().map(Purchase::toView).toList();
    }

    private Purchase findPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Purchase not found", HttpStatus.NOT_FOUND));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Product with id %d not found", productId), HttpStatus.NOT_FOUND));
    }

    private Supplier findSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Supplier with id %d not found", supplierId), HttpStatus.NOT_FOUND));
    }
}
