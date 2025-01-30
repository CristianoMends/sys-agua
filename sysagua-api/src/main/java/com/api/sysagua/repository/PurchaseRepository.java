package com.api.sysagua.repository;

import com.api.sysagua.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("""
            select p from Purchase 
            p inner join p.productPurchases productPurchases
            where (:id is null or p.id = :id) 
            and ((:totalValueStart is null or :totalValueEnd is null) or p.totalValue between :totalValueStart and :totalValueEnd) 
            and (:active is null or p.active = :active) 
            and ((CAST(:updatedAtStart AS timestamp) is null or CAST(:updatedAtEnd AS timestamp) is null) or p.updatedAt between :updatedAtStart and :updatedAtEnd) 
            and ((CAST(:createdAtStart AS timestamp) is null or CAST(:createdAtEnd AS timestamp) is null) or p.createdAt between :createdAtStart and :createdAtEnd) 
            and (:supplierId is null or p.supplier.id = :supplierId) 
            and (:productId is null or productPurchases.product.id = :productId)
            order by p.createdAt
            """)
    List<Purchase> list(
            @Param("id") Long id,
            @Param("totalValueStart") BigDecimal totalValueStart,
            @Param("totalValueEnd") BigDecimal totalValueEnd,
            @Param("active") Boolean active,
            @Param("updatedAtStart") LocalDateTime updatedAtStart,
            @Param("updatedAtEnd") LocalDateTime updatedAtEnd,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            @Param("supplierId") Long supplierId,
            @Param("productId") Long productId);


}
