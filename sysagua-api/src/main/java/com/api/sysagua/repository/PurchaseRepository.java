package com.api.sysagua.repository;

import com.api.sysagua.enumeration.PaymentMethod;
import com.api.sysagua.enumeration.PaymentStatus;
import com.api.sysagua.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("""
            select p from Purchase p 
            inner join p.productPurchases productPurchases
            where (:id is null or p.id = :id) 
            and ((:totalAmountStart is null or :totalAmountEnd is null) or p.total between :totalAmountStart and :totalAmountEnd) 
            and ((:paidAmountStart is null or :paidAmountEnd is null) or p.paidAmount between :paidAmountStart and :paidAmountEnd) 
            and (:active is null or p.active = :active) 
            and ((CAST(:entryAtStart AS timestamp) is null or CAST(:entryAtEnd AS timestamp) is null) or p.entryAt between :entryAtStart and :entryAtEnd) 
            and ((CAST(:createdAtStart AS timestamp) is null or CAST(:createdAtEnd AS timestamp) is null) or p.createdAt between :createdAtStart and :createdAtEnd)
            and ((CAST(:finishedAtStart AS timestamp) is null or CAST(:finishedAtEnd AS timestamp) is null) or p.finishedAt between :finishedAtStart and :finishedAtEnd)
            and ((CAST(:canceledAtStart AS timestamp) is null or CAST(:canceledAtEnd AS timestamp) is null) or p.canceledAt between :canceledAtStart and :canceledAtEnd)
            and ((:description is null or :description = '') or lower(p.description) like lower(concat('%', :description, '%')))
            and (:nfe is null or p.nfe = :nfe)
            and (:paymentMethod is null or p.paymentMethod = :paymentMethod)
            and (:paymentStatus is null or p.paymentStatus = :paymentStatus)
            and (:supplierId is null or p.supplier.id = :supplierId) 
            and (:productId is null or productPurchases.product.id = :productId)
            order by p.createdAt
            """)
    List<Purchase> list(
            @Param("id") Long id,
            @Param("totalAmountStart") BigDecimal totalAmountStart,
            @Param("totalAmountEnd") BigDecimal totalAmountEnd,
            @Param("paidAmountStart") BigDecimal paidAmountStart,
            @Param("paidAmountEnd") BigDecimal paidAmountEnd,
            @Param("active") Boolean active,
            @Param("entryAtStart") LocalDateTime entryAtStart,
            @Param("entryAtEnd") LocalDateTime entryAtEnd,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            @Param("finishedAtStart") LocalDateTime finishedAtStart,
            @Param("finishedAtEnd") LocalDateTime finishedAtEnd,
            @Param("canceledAtStart") LocalDateTime canceledAtStart,
            @Param("canceledAtEnd") LocalDateTime canceledAtEnd,
            @Param("description") String description,
            @Param("supplierId") Long supplierId,
            @Param("productId") Long productId,
            @Param("nfe") String nfe,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("paymentStatus") PaymentStatus paymentStatus
    );

}
