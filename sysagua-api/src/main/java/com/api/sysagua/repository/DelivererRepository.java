package com.api.sysagua.repository;

import com.api.sysagua.model.Deliverer;
import com.api.sysagua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DelivererRepository extends
        JpaRepository<Deliverer, Long>{

    Optional<User> findByPhone(String phone);

    @Query("""
                select d from Deliverer d
                where   (:id IS NULL OR d.id = :id) 
                and     ((:name IS NULL OR :name = '') OR upper(d.name) like upper(concat('%', :name, '%'))) 
                and     ((:phone IS NULL OR :phone = '') OR upper(d.phone) = upper(:phone))  
                order by d.name
            """)

    List<Deliverer> findByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("phone") String phone
    );
}
