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
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(user -> {
                    throw new BusinessException("There is already a user with the e-mail " + userDto.getEmail());
                });

        // Verifica se já existe um usuário com o número de telefone fornecido
        userRepository.findByPhone(userDto.getPhone())
                .ifPresent(user -> {
                    throw new BusinessException("There is already a user with the phone " + userDto.getPhone());
                });

        userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        return userRepository.save(userDto.toModel());
    }

    @Override
    public UserDetails findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow();
    }


}
