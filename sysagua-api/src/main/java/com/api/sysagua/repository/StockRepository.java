package com.api.sysagua.repository;

import com.api.sysagua.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("""
            select s from Stock s
            where (:id is null or s.id = :id) 
            and ((:initialQuantityStart is null or :initialQuantityEnd is null) or s.initialQuantity between :initialQuantityStart and :initialQuantityEnd) 
            and ((:totalEntriesStart is null or :totalEntriesEnd is null) or s.totalEntries between :totalEntriesStart and :totalEntriesEnd) 
            and ((:totalWithdrawalsStart is null or :totalWithdrawalsEnd is null) or s.totalWithdrawals between :totalWithdrawalsStart and :totalWithdrawalsEnd) 
            and ((CAST(:createdAtStart AS timestamp) is null or CAST(:createdAtEnd AS timestamp) is null) or s.createdAt between :createdAtStart and :createdAtEnd )
            and ((CAST(:updatedAtStart AS timestamp) is null or CAST(:updatedAtEnd AS timestamp) is null) or s.updatedAt between :updatedAtStart and :updatedAtEnd )
            and (:productId is null or s.product.id = :productId )
            and (:productName is null or s.product.name like concat('%', :productName, '%'))
            order by s.createdAt""")
    List<Stock> list(
            @Param("id") Long id,
            @Param("initialQuantityStart") Integer initialQuantityStart,
            @Param("initialQuantityEnd") Integer initialQuantityEnd,
            @Param("totalEntriesStart") Integer totalEntriesStart,
            @Param("totalEntriesEnd") Integer totalEntriesEnd,
            @Param("totalWithdrawalsStart") Integer totalWithdrawalsStart,
            @Param("totalWithdrawalsEnd") Integer totalWithdrawalsEnd,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            @Param("updatedAtStart") LocalDateTime updatedAtStart,
            @Param("updatedAtEnd") LocalDateTime updatedAtEnd,
            @Param("productId") Long productId,
            @Param("productName") String productName);


    @Query("select s from Stock s where s.product.id = :id")
    Optional<Stock> findProduct(@Param("id") Long id);


}
