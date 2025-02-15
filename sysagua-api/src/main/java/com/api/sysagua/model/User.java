package com.api.sysagua.model;

import com.api.sysagua.dto.user.ViewUserDto;
import com.api.sysagua.enumeration.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.UUID;
import com.api.sysagua.enumeration.UserAccess;

@Table(name="Users")
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

    @Column(nullable = false,length = 20)
    private String name;

    @Column(length = 30)
    private String surname;

    @Column(unique = true,length = 11)
    private String phone;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAccess access;

    public User(String name, String surname, String phone,String email, String password, UserStatus status, UserAccess access) {
        setName(name);
        setSurname(surname);
        setEmail(email);
        setPhone(phone);
        setPassword(password);
        setStatus(status);
        setAccess(access);
    }

    public ViewUserDto toView(){
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
        if(this.access.equals(UserAccess.DEVELOPER)){
            return List.of(
                    new SimpleGrantedAuthority("ROLE_OWNER"),
                    new SimpleGrantedAuthority("ROLE_EMPLOYEE"),
                    new SimpleGrantedAuthority("ROLE_MANAGER"),
                    new SimpleGrantedAuthority("ROLE_DEVELOPER")
                    );
        }
        else if (this.access.equals(UserAccess.MANAGER)){return List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));}
        else if (this.access.equals(UserAccess.EMPLOYEE)){return List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));}
        else {return List.of(new SimpleGrantedAuthority("ROLE_OWNER"));}
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