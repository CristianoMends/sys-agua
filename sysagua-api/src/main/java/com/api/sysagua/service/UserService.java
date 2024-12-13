package com.api.sysagua.service;


import com.api.sysagua.dto.CreateUserDto;
import com.api.sysagua.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    User createUser(CreateUserDto userDto);

    UserDetails findByEmail(String email);
}
