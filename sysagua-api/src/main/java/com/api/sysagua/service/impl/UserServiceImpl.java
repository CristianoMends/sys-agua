package com.api.sysagua.service.impl;


import com.api.sysagua.dto.CreateUserDto;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.User;
import com.api.sysagua.repository.UserRepository;
import com.api.sysagua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(CreateUserDto userDto) {
        if(this.findByEmail(userDto.getEmail()) != null){
            throw new BusinessException("There is already a user with the email "+ userDto.getEmail());
        }
        if (this.userRepository.findByPhone(userDto.getPhone()) != null){
            throw new BusinessException("There is already a phone number "+userDto.getPhone());
        }

        userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        return userRepository.save(userDto.toModel());
    }

    @Override
    public UserDetails findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }


}
