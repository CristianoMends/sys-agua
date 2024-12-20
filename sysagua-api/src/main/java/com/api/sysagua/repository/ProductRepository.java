package com.api.sysagua.repository;

import com.api.sysagua.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
        select p from Product p
        where   (:id                is null or p.id = :id)
        and     upper(p.name) like upper(concat('%', :name, '%'))
        and     upper(p.category) like upper(concat('%', :category, '%'))
        and     upper(p.unit) like upper(concat('%', :unit, '%'))
        and     upper(p.brand) like upper(concat('%', :brand, '%'))
        and     ((:registerUpdateInit is null or :registerUpdateEnd is null) or p.updatedAt between :registerUpdateInit and :registerUpdateEnd)
        and     ((:registerDateInit is null or :registerDateEnd is null) or p.registeredAt between :registerDateInit and :registerDateEnd)
        and     ((:minCost          is null or :maxCost  is null) or p.cost between :minCost and :maxCost)
        and     ((:minPrice         is null or :maxPrice is null) or p.price between :minPrice and :maxPrice)
        order by p.name, p.category
    """)
    List<Product> findByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("category") String category,
            @Param("unit") String unit,
            @Param("brand") String brand,
            @Param("registerUpdateInit") LocalDate registerUpdateInit,
            @Param("registerUpdateEnd") LocalDate registerUpdateEnd,
            @Param("registerDateInit") LocalDate registerDateInit,
            @Param("registerDateEnd") LocalDate registerDateEnd,
            @Param("minCost") Double minCost,
            @Param("maxCost") Double maxCost,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );



}
