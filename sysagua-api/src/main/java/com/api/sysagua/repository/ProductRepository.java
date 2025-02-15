package com.api.sysagua.repository;

import com.api.sysagua.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
                select p from Product p
                where ( :id                is null or p.id = :id )
                and   ( (CAST(:updatedAtStart AS timestamp) is null or CAST(:updatedAtEnd AS timestamp) is null) or p.updatedAt between :updatedAtStart and :updatedAtEnd)
                and   ( (CAST(:createdAtInit AS timestamp) is null or CAST(:createdAtEnd AS timestamp) is null) or p.createdAt between :createdAtInit and :createdAtEnd)
                and   ( upper(p.name) like upper(concat('%', :name, '%')) )
                and   ( (:priceStart is null or :priceEnd is null) or p.price between :priceStart and :priceEnd) 
                and   ( (:costStart is null or :costEnd is null) or p.cost between :costStart and :costEnd ) 
                and   ( upper(p.category.name) like upper(concat('%', :category, '%')) )
                and   ( upper(p.line.name) like upper(concat('%', :line, '%')) )
                and   ( upper(p.ncm) like upper(concat('%', :ncm, '%'))      )
                and   ( upper(p.unit) like upper(concat('%', :unit, '%'))    )
                and   ( upper(p.brand) like upper(concat('%', :brand, '%'))  )                
                and   ( :active is null or p.active = :active )
                order by p.name DESC
            """)
    List<Product> findByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("priceStart") BigDecimal priceStart,
            @Param("priceEnd") BigDecimal priceEnd,
            @Param("costStart") BigDecimal costStart,
            @Param("costEnd") BigDecimal costEnd,
            @Param("category") String category,
            @Param("unit") String unit,
            @Param("brand") String brand,
            @Param("updatedAtStart") LocalDateTime updatedAtStart,
            @Param("updatedAtEnd") LocalDateTime updatedAtEnd,
            @Param("createdAtInit") LocalDateTime createdAtInit,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            @Param("active") Boolean active,
            @Param("ncm") String ncm,
            @Param("line") String line
    );


}
