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
        and     ((:updatedAtStart is null or :updatedAtEnd is null) or p.updatedAt between :updatedAtStart and :updatedAtEnd)
        and     ((:createdAtInit is null or :createdAtEnd is null) or p.createdAt between :createdAtInit and :createdAtEnd)
        and     (:active is null or p.active = :active)
        order by p.createdAt, p.name, p.category
    """)
    List<Product> findByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("category") String category,
            @Param("unit") String unit,
            @Param("brand") String brand,
            @Param("updatedAtStart") LocalDate updatedAtStart,
            @Param("updatedAtEnd") LocalDate updatedAtEnd,
            @Param("createdAtInit") LocalDate createdAtInit,
            @Param("createdAtEnd") LocalDate createdAtEnd,
            @Param("active") Boolean active
    );



}
