package com.api.sysagua.repository;

import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    @Query("""
                select u from User u
                where (:id is null or u.id = :id)
                and upper(u.name) like upper(concat('%', :name, '%'))
                and (:surname = '' or upper(u.surname) like upper(concat('%', :surname, '%')))
                and (:phone = '' or upper(u.phone) like upper(concat('%', :phone, '%')))
                and upper(u.email) like upper(concat('%', :email, '%'))
                and (:status is null or u.status = :status)
                and (:access is null or u.access = :access)
                order by u.name, u.status
            """)
    List<User> findByFilters(
            @Param("id") UUID id,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("status") UserStatus status,
            @Param("access") UserAccess access
    );


}
