package com.api.sysagua.service.impl;

import com.api.sysagua.dto.purchase.*;
import com.api.sysagua.dto.stock.AddProductDto;
import com.api.sysagua.dto.transaction.CreateTransactionDto;
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

        checkPaimentValue(purchase);

        if (isPaid(purchase)) {
            purchase.setPaymentStatus(PaymentStatus.PAID);
            purchase.setFinishedAt(LocalDateTime.now());
        } else {
            purchase.setPaymentStatus(PaymentStatus.PENDING);
        }

        var saved = this.purchaseRepository.save(purchase);

        if (dto.getPaidAmount() != null) {
            createTransaction(saved, dto.getPaidAmount().negate(), dto.getPaymentMethod(), "Compra registrada");
        }

        processProductsOnStock(saved);
    }

    @Override
    @Transactional
    public void addPayment(Long id, CreateTransactionDto dto) {
        var purchase = this.purchaseRepository.findById(id).orElseThrow(() -> new BusinessException("Purchase not found", HttpStatus.NOT_FOUND));
        if (!purchase.getActive() || isPaid(purchase)) return;

        purchase.setPaidAmount(purchase.getPaidAmount().add(dto.getAmount()));

        checkPaimentValue(purchase);

        if (isPaid(purchase)) {
            purchase.setPaymentStatus(PaymentStatus.PAID);
        } else {
            purchase.setPaymentStatus(PaymentStatus.PENDING);
        }

        var saved = this.purchaseRepository.save(purchase);
        createTransaction(saved, dto.getAmount().negate(), dto.getPaymentMethod(), dto.getDescription());
    }

    @Override
    public void delete(Long id) {
        var purchase = this.purchaseRepository.findById(id).orElseThrow(() -> new BusinessException("Purchase not found", HttpStatus.NOT_FOUND));
        purchase.setActive(false);
        purchase.setPaymentStatus(PaymentStatus.CANCELED);
        purchase.setCanceledAt(LocalDateTime.now());

        var saved = this.purchaseRepository.save(purchase);

        createTransaction(purchase, purchase.getPaidAmount(), PaymentMethod.UNDEFINED, "Estorno de pagamentos");
        processProductRefunds(saved);
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

    private void checkPaimentValue(Purchase purchase) {
        purchase.calculateTotalAmount();
        if (purchase.getPaidAmount().compareTo(purchase.getTotalAmount()) > 0) {
            throw new BusinessException("Amount paid is greater than the total purchase amount");
        }
    }

    private boolean isPaid(Purchase purchase) {
        if (purchase.getTotalAmount() == null) purchase.calculateTotalAmount();

        return purchase.getPaidAmount().compareTo(purchase.getTotalAmount()) >= 0;
    }

    private List<ProductPurchase> createProductPurchase(List<CreateProductPurchaseDto> dto, Purchase purchase) {
        List<ProductPurchase> productPurchases = new ArrayList<>();
        for (var item : dto) {
            var product = this.findProductById(item.getProductId());
            productPurchases.add(new ProductPurchase(purchase, product, item.getQuantity(), item.getPurchasePrice()));
        }
        return productPurchases;
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
        this.stockService.addProduct(new AddProductDto(product.getId(), quantity));
    }

    private void remEntriesProductOnStock(int quantity, Product product) {
        this.stockService.removeProduct(new AddProductDto(product.getId(), quantity));
    }

    private void processProductsOnStock(Purchase purchase) {
        purchase.getProductPurchases()
                .forEach(p -> addEntriesProductOnStock(p.getQuantity(), p.getProduct()));

    }

    private void processProductRefunds(Purchase purchase) {
        purchase.getProductPurchases()
                .forEach(p -> remEntriesProductOnStock(p.getQuantity(), p.getProduct()));
    }

    private void createTransaction(Purchase purchase, BigDecimal amount, PaymentMethod paymentMethod, String description) {

        var user = this.userService.getLoggedUser();
        var t = new Transaction(
                amount,
                TransactionType.EXPENSE,
                paymentMethod,
                description,
                user,
                purchase
        );
        this.transactionRepository.save(t);
    }
}
