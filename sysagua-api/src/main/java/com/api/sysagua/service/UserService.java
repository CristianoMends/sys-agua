package com.api.sysagua.service;

import com.api.sysagua.dto.user.*;
import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User registerUser(CreateUserDto userDto);

    UserDetails getUserByEmail(String email);

    void deactivateUser(String email);

    Token authenticateUser(LoginDto loginDto);

    List<User> getUsers(UUID id,
                        String name,
                        String surname,
                        String phone,
                        String email,
                        UserStatus status,
                        UserAccess access);

    void updateUser(UpdateUserDto userDto);

    String getLoggedUser();
}
