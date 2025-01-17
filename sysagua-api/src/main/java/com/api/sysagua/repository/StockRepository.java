package com.api.sysagua.repository;

import com.api.sysagua.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("""
            select s from Stock s
            where (:id is null or s.id = :id) 
            and (
                    (:quantityStart is null or :quantityEnd is null) or s.quantity between :quantityStart and :quantityEnd
                ) 
            and (
                    (:exitsStart is null or :exitsEnd is null) or s.exits between :exitsStart and :exitsEnd
                ) 
            and (
                    (:addedAtStart is null or :addedAtEnd is null) or s.addedAt between :addedAtStart and :addedAtEnd
                ) 
            and (
                    (:entriesStart is null or :entriesEnd is null) or s.entries between :entriesStart and :entriesEnd
                )
            and (
                    :productId is null or s.product.id = :productId 
                )
            order by s.updatedAt ASC
            """)
    List<Stock> findByFilters(
            @Param("id") Long id,
            @Param("quantityStart") Integer quantityStart,
            @Param("quantityEnd") Integer quantityEnd,
            @Param("exitsStart") Integer exitsStart,
            @Param("exitsEnd") Integer exitsEnd,
            @Param("addedAtStart") LocalDate addedAtStart,
            @Param("addedAtEnd") LocalDate addedAtEnd,
            @Param("entriesStart") Integer entriesStart,
            @Param("entriesEnd") Integer entriesEnd,
            @Param("productId") Long productId
            );

    @Query("select s from Stock s where s.product.id = :id")
    Optional<Stock> findProduct(@Param("id") Long id);


}
