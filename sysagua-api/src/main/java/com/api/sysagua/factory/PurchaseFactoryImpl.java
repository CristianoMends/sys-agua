package com.api.sysagua.factory;

import com.api.sysagua.dto.purchase.CreateProductPurchaseDto;
import com.api.sysagua.dto.purchase.CreatePurchaseDto;
import com.api.sysagua.enumeration.PaymentStatus;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.Product;
import com.api.sysagua.model.ProductPurchase;
import com.api.sysagua.model.Purchase;
import com.api.sysagua.repository.ProductRepository;
import com.api.sysagua.repository.PurchaseRepository;
import com.api.sysagua.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseFactoryImpl implements PurchaseFactory {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Purchase createPurchase(CreatePurchaseDto dto) {
        var purchase = new Purchase();
        var supplier = supplierRepository.findById(dto.getSupplierId()).orElseThrow();
        if (!supplier.getActive()) throw new BusinessException("Supplier is inactive");
        purchase.setSupplier(supplier);
        purchase.setProductPurchases(createProductPurchases(dto.getItems(), purchase));
        purchase.setPaymentMethod(dto.getPaymentMethod());
        purchase.setPaidAmount(dto.getPaidAmount());
        purchase.setNfe(dto.getNfe());
        purchase.setEntryAt(dto.getEntryAt());
        purchase.setPaymentStatus(PaymentStatus.PENDING);
        purchase.setActive(true);
        purchase.setDescription(dto.getDescription());
        return purchase;
    }

    private List<ProductPurchase> createProductPurchases(List<CreateProductPurchaseDto> productDtos, Purchase purchase) {
        List<ProductPurchase> productPurchases = new ArrayList<>();
        productDtos.forEach(dto -> {
            Product product = productRepository.findById(dto.getProductId()).orElseThrow();
            productPurchases.add(new ProductPurchase(purchase, product, dto.getQuantity(), dto.getPurchasePrice()));
        });
        return productPurchases;
    }
}
