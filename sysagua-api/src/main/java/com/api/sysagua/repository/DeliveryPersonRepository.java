package com.api.sysagua.repository;

import com.api.sysagua.model.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    Optional<DeliveryPerson> findByPhone(String phone);

    @Query("""
            select d from DeliveryPerson d
            where (:id is null or d.id = :id) 
            and     ((:name IS NULL OR :name = '') OR upper(d.name) like upper(concat('%', :name, '%'))) 
            and     ((:phone IS NULL OR :phone = '') OR upper(d.phone) = upper(:phone))  
            and (:active is null or d.active = :active) 
            and ((:createdAtStart is null or :createdAtEnd is null) or d.createdAt between :createdAtStart and :createdAtEnd)
            order by d.createdAt
            """)
    List<DeliveryPerson> findByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("active") Boolean active,
            @Param("createdAtStart") LocalDate createdAtStart,
            @Param("createdAtEnd") LocalDate createdAtEnd);
}
