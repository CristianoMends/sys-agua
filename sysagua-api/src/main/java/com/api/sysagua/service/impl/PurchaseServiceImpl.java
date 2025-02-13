package com.api.sysagua.service.impl;

import com.api.sysagua.dto.purchase.*;
import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.enumeration.*;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.*;
import com.api.sysagua.repository.*;
import com.api.sysagua.service.PurchaseService;
import com.api.sysagua.service.StockService;
import com.api.sysagua.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private StockService stockService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private StockHistoryRepository stockHistoryRepository;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
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
        purchase.setEntryAt(dto.getEntryAt());
        purchase.setNfe(dto.getNfe());
        purchase.setPaidAmount(dto.getPaidAmount());
        purchase.setPaymentStatus(PaymentStatus.PENDING);

        switch (checkPaymentStatus(purchase)) {
            case PAID -> {
                purchase.setPaymentStatus(PaymentStatus.PAID);
                purchase.setFinishedAt(LocalDateTime.now());
            }
            case PENDING -> purchase.setPaymentStatus(PaymentStatus.PENDING);
        }

        var saved = this.purchaseRepository.save(purchase);

        switch (saved.getPaymentStatus()) {
            case PAID -> createPaidTransaction(saved);
            case PENDING -> createPendingTransaction(saved);
        }

        processProductsOnStock(saved);
    }

    @Override
    @Transactional
    public void update(Long id, UpdatePurchaseDto dto) {
        Purchase purchase = findPurchaseById(id);

        if (dto.getSupplierId() != null) {
            var supplier = findSupplierById(dto.getSupplierId());
            purchase.setSupplier(supplier);
        }

        if (dto.getNfe() != null) purchase.setNfe(dto.getNfe());
        if (dto.getEntryAt() != null) purchase.setEntryAt(dto.getEntryAt());
        if (dto.getDescription() != null) purchase.setDescription(dto.getDescription());
        if (dto.getPaymentMethod() != null) purchase.setPaymentMethod(dto.getPaymentMethod());
        if (dto.getPaymentStatus() != null) purchase.setPaymentStatus(dto.getPaymentStatus());
        var differenceAmounts = BigDecimal.ZERO;
        if (dto.getPaidAmount() != null) {
            if (dto.getPaidAmount().compareTo(purchase.getPaidAmount()) <= 0)
                throw new BusinessException("amount paid must be greater than the previous one");

            differenceAmounts = dto.getPaidAmount().subtract(purchase.getPaidAmount());
            purchase.setPaidAmount(dto.getPaidAmount());
        }
        if (dto.getTotalAmount() != null) purchase.setTotalAmount(dto.getTotalAmount());

        switch (checkPaymentStatus(purchase)) {
            case PAID -> {
                purchase.setPaymentStatus(PaymentStatus.PAID);
                purchase.setFinishedAt(LocalDateTime.now());
            }
            case CANCELED -> {
                purchase.setCanceledAt(LocalDateTime.now());
                purchase.setPaymentStatus(PaymentStatus.CANCELED);
            }
            case PENDING -> purchase.setPaymentStatus(PaymentStatus.PENDING);

        }
        var saved = this.purchaseRepository.save(purchase);

        switch (purchase.getPaymentStatus()) {
            case PENDING ->
                    createTransaction(saved, TransactionStatus.PENDING, differenceAmounts, TransactionType.EXPENSE);
            case CANCELED -> createCanceledTransaction(saved);
            case PAID -> createPaidTransaction(saved);
        }

    }

    @Override
    public void delete(Long id) {
        Purchase purchase = findPurchaseById(id);
        purchase.setActive(false);
        var saved = this.purchaseRepository.save(purchase);
        createCanceledTransaction(saved);
    }

    @Override
    public List<ViewPurchaseDto> list(
            Long id,
            BigDecimal totalAmountStart,
            BigDecimal totalAmountEnd,
            BigDecimal paidAmountStart,
            BigDecimal paidAmountEnd,
            Boolean active,
            LocalDateTime entryAtStart,
            LocalDateTime entryAtEnd,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime finishedAtStart,
            LocalDateTime finishedAtEnd,
            LocalDateTime canceledAtStart,
            LocalDateTime canceledAtEnd,
            String description,
            Long supplierId,
            Long productId,
            String nfe,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus
    ) {
        return purchaseRepository.list(
                id,
                totalAmountStart,
                totalAmountEnd,
                paidAmountStart,
                paidAmountEnd,
                active,
                entryAtStart,
                entryAtEnd,
                createdAtStart,
                createdAtEnd,
                finishedAtStart,
                finishedAtEnd,
                canceledAtStart,
                canceledAtEnd,
                description,
                supplierId,
                productId,
                nfe,
                paymentMethod,
                paymentStatus
        ).stream().map(Purchase::toView).toList();
    }

    private PaymentStatus checkPaymentStatus(Purchase purchase) {
        if (purchase.getTotalAmount() == null) purchase.updateTotalValue();

        if (purchase.getPaymentStatus().equals(PaymentStatus.CANCELED)) return PaymentStatus.CANCELED;


        if (purchase.getPaidAmount().compareTo(purchase.getTotalAmount()) >= 0) {
            return PaymentStatus.PAID;
        }

        return PaymentStatus.PENDING;
    }

    private List<ProductPurchase> createProductPurchase(List<CreateProductPurchaseDto> dto, Purchase purchase) {
        List<ProductPurchase> productPurchases = new ArrayList<>();
        for (var item : dto) {
            var product = this.findProductById(item.getProductId());
            productPurchases.add(new ProductPurchase(purchase, product, item.getQuantity(), item.getPurchasePrice()));
        }
        return productPurchases;
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

    private void addEntriesProductOnStock(int quantity, Product product) {
        this.stockService.addProduct(new AddProductDto(product.getId(),quantity));
    }

    private void processProductsOnStock(Purchase purchase) {
        purchase.getProductPurchases()
                .forEach(p -> addEntriesProductOnStock(p.getQuantity(), p.getProduct()));

    }

    private void createPendingTransaction(Purchase purchase) {
        var amountPending = purchase.getTotalAmount().subtract(purchase.getPaidAmount());
        var description = "Quantia paga R$ " + purchase.getPaidAmount() + ", Quantia pendente R$" + amountPending;
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                TransactionStatus.PENDING,
                purchase.getPaidAmount(),
                TransactionType.EXPENSE,
                description,
                user,
                null,
                purchase
        );
        this.transactionRepository.save(t);
    }

    private void createTransaction(Purchase purchase, TransactionStatus status, BigDecimal amout, TransactionType type) {
        var amountPending = purchase.getTotalAmount().subtract(purchase.getPaidAmount());
        var description = "Quantia paga R$ " + amout + ", Quantia pendente R$" + amountPending;
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                status,
                amout,
                type,
                description,
                user,
                null,
                purchase
        );
        this.transactionRepository.save(t);
    }

    private void createPaidTransaction(Purchase purchase) {
        var description = "Quantia paga R$ " + purchase.getPaidAmount() + ", de valor total R$" + purchase.getTotalAmount();
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                TransactionStatus.PAID,
                purchase.getPaidAmount(),
                TransactionType.EXPENSE,
                description,
                user,
                null,
                purchase
        );
        this.transactionRepository.save(t);
    }

    private void createCanceledTransaction(Purchase purchase) {
        var description = "Compra cancelada. Estorno de transações realizado";
        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                TransactionStatus.CANCELED,
                purchase.getPaidAmount(),
                TransactionType.INCOME,
                description,
                user,
                null,
                purchase
        );
        this.transactionRepository.save(t);
    }
}
