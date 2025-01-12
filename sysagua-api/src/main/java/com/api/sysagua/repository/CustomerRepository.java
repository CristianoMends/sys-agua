package com.api.sysagua.repository;

import com.api.sysagua.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = """
                select c from Customer c
                where   (:id IS NULL OR c.id = :id) 
                and     ((:name IS NULL OR :name = '') OR upper(c.name) like upper(concat('%', :name, '%'))) 
                and     ((:phone IS NULL OR :phone = '') OR upper(c.phone) = upper(:phone))
                and     ((:street IS NULL OR :street = '') OR upper(c.address.street) like upper(concat('%', :street, '%')))
                and     ((:neighborhood IS NULL OR :neighborhood = '') OR upper(c.address.neighborhood) like upper(concat('%', :neighborhood, '%')))
                and     ((:city IS NULL OR :city = '') OR upper(c.address.city) like upper(concat('%', :city, '%')))
                and     ((:state IS NULL OR :state = '') OR upper(c.address.state) like upper(concat('%', :state, '%')))
                and     (:active IS NULL OR c.active = :active)
                and     (:cnpj IS NULL OR c.cnpj = :cnpj)  
                order by c.name
            """)
    List<Customer> findByFilters(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("street") String street,
            @Param("neighborhood") String neighborhood,
            @Param("city") String city,
            @Param("state") String state,
            @Param("active") Boolean active,
            @Param("cnpj") String cnpj
    );

}
