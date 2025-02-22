package com.api.sysagua.model;

import com.api.sysagua.dto.user.ViewUserDto;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.security.PasswordEncoderSingleton;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.api.sysagua.enumeration.UserAccess;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Table(name = "Users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 30)
    private String surname;

    @Column(unique = true, length = 11)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAccess access;

    public static class Builder {
        private UUID id;
        private String name;
        private String surname;
        private String phone;
        private String email;
        private String password;
        private UserStatus status;
        private UserAccess access;

        private final BCryptPasswordEncoder encoder = PasswordEncoderSingleton.getInstance().getEncoder();

        public Builder() {
        }

        public static Builder fromUser(User user) {
            return new Builder()
                    .withId(user.getId())
                    .withName(user.getName())
                    .withSurname(user.getSurname())
                    .withPhone(user.getPhone())
                    .withEmail(user.getEmail())
                    .withPassword(user.getPassword())
                    .withStatus(user.getStatus())
                    .withAccess(user.getAccess());
        }

        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            Optional.ofNullable(name).ifPresent(value -> this.name = value);
            return this;
        }

        public Builder withSurname(String surname) {
            Optional.ofNullable(surname).ifPresent(value -> this.surname = value);
            return this;
        }

        public Builder withPhone(String phone) {
            Optional.ofNullable(phone).ifPresent(value -> this.phone = value);
            return this;
        }

        public Builder withEmail(String email) {
            Optional.ofNullable(email).ifPresent(value -> this.email = value);
            return this;
        }

        public Builder withPassword(String password) {
            Optional.ofNullable(password).ifPresent(value -> this.password = encoder.encode(value));
            return this;
        }

        public Builder withStatus(UserStatus status) {
            Optional.ofNullable(status).ifPresent(value -> this.status = value);
            return this;
        }

        public Builder withAccess(UserAccess access) {
            Optional.ofNullable(access).ifPresent(value -> this.access = value);
            return this;
        }

        public User build() {
            return new User(id, name, surname, phone, email, password, status, access);
        }
    }

    public User(UUID id, String name, String surname, String phone, String email, String password, UserStatus status, UserAccess access) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.status = status;
        this.access = access;
    }

    public ViewUserDto toView() {
        return new ViewUserDto(
                getId(),
                getName(),
                getSurname(),
                getPhone(),
                getEmail(),
                getStatus(),
                getAccess()

        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.access.equals(UserAccess.MANAGER)) {
            return List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));
        } else if (this.access.equals(UserAccess.EMPLOYEE)) {
            return List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_OWNER"));
        }
    }

    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public String getUsername() {
        return this.email;
    }
}