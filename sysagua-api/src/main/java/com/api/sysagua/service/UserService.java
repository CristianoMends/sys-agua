package com.api.sysagua.service;

import com.api.sysagua.dto.*;
import com.api.sysagua.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    User registerUser(CreateUserDto userDto);

    UserDetails getUserByEmail(String email);

    void deactivateUser(String email);

    Token authenticateUser(LoginDto loginDto);

    List<User> getUsers(SearchUserDto userDto);

    User updateUser(UpdateUserDto userDto);
}
