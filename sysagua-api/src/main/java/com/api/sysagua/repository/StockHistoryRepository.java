package com.api.sysagua.repository;

import com.api.sysagua.enumeration.MovementType;
import com.api.sysagua.model.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    @Query("""
            select s from StockHistory s
            where (:id is null or s.id = :id ) 
            and (:type is null or s.type = :type ) 
            and ((:quantityStart is null or :quantityEnd is null) or s.quantity between :quantityStart and :quantityEnd ) 
            and ((CAST(:dateStart AS TIMESTAMP) is null or CAST(:dateEnd AS TIMESTAMP) is null) or s.date between :dateStart and :dateEnd ) 
            and (:description is null or s.description like concat('%', :description, '%') ) 
            and (:responsibleUserId is null or s.responsibleUser.id = :responsibleUserId ) 
            and (:stockId is null or s.stock.id = :stockId )
            and (:productId is null or s.stock.product.id = :productId )
            order by s.date""")
    List<StockHistory> list(
            @Param("id") Long id,
            @Param("type") MovementType type,
            @Param("quantityStart") Integer quantityStart,
            @Param("quantityEnd") Integer quantityEnd,
            @Param("dateStart") LocalDateTime dateStart,
            @Param("dateEnd") LocalDateTime dateEnd,
            @Param("description") String description,
            @Param("responsibleUserId") UUID responsibleUserId,
            @Param("stockId") Long stockId,
            @Param("productId") Long productId
    );


}
