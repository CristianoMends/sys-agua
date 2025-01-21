package com.api.sysagua.repository;

import com.api.sysagua.model.ProductLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductLineRepository extends JpaRepository<ProductLine, Long> {
    @Query("select p from ProductLine p " +
            "where (:id is null or p.id = :id) " +
            "and (:active is null or p.active = :active)" +
            "and (:name is null or upper(p.name) like upper(concat('%', :name, '%')))")
    List<ProductLine> list(@Param("id") Long id,
                           @Param("active") Boolean active,
                           @Param("name") String name);


}
