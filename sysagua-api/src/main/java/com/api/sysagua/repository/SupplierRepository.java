package com.api.sysagua.repository;

import com.api.sysagua.model.Supplier;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findByCnpj(String cnpj);

    Optional<Supplier> findByPhone(String phone);

    @Query("""
            select s from Supplier s
            where (:id          is null or s.id = :id)
            and ((:socialReason  is null or :socialReason = '')  or upper(s.socialReason) like upper(concat('%', :socialReason, '%')))
            and ((:cnpj          is null or :cnpj = '') or upper(s.cnpj) = upper(:cnpj))
            and ((:phone         is null or :phone = '') or upper(s.phone) = upper(:phone))
            and (:active          is null or s.active = :active)
            and ((:tradeName is null or :tradeName = '') or s.tradeName = :tradeName)
            and ((:stateRegistration is null or :stateRegistration = '') or s.stateRegistration = :stateRegistration)
            and ((:municipalRegistration is null or :municipalRegistration = '') or s.municipalRegistration = :municipalRegistration)
            order by s.socialReason DESC
            """)
    List<Supplier> findByFilters(
            @Param("id") Long id,
            @Param("socialReason") String socialReason,
            @Param("cnpj") String cnpj,
            @Param("phone") String phone,
            @Param("active") Boolean active,
            @Param("tradeName") String tradeName,
            @Param("stateRegistration") String stateRegistration,
            @Param("municipalRegistration") String municipalRegistration
    );

}
