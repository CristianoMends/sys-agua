package com.api.sysagua.repository;

import com.api.sysagua.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {

    @Query("select p from ProductCategory p " +
            "where (:id is null or p.id = :id) " +
            "and (:active is null or p.active = :active)" +
            "and (:name is null or upper(p.name) like concat('%', upper(:name), '%'))")
    List<ProductCategory> list(
            @Param("id") Long id,
            @Param("active") Boolean active,
            @Param("name") String name);


}
