package com.api.sysagua.repository;

import com.api.sysagua.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<UserDetails> findByEmail(String email);

    Optional<User> findByPhone(String phone);
}