package com.api.sysagua.service.impl;

import com.api.sysagua.dto.user.*;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.User;
import com.api.sysagua.repository.UserRepository;
import com.api.sysagua.service.TokenService;
import com.api.sysagua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User registerUser(CreateUserDto userDto) {
        checkIfEmailExists(userDto.getEmail());
        checkIfPhoneExists(userDto.getPhone());

        var userToSave = userDto.toModel();
        userToSave.setPassword(new BCryptPasswordEncoder().encode(userToSave.getPassword()));

        return userRepository.save(userToSave);
    }

    private void checkIfEmailExists(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.getStatus().equals(UserStatus.INACTIVE)) {
                throw new BusinessException(
                        "The email provided is already associated with a user who has been deleted.",
                        HttpStatus.CONFLICT
                );
            }
            throw new BusinessException(
                    "There is already a user with the e-mail " + email,
                    HttpStatus.CONFLICT
            );
        });
    }

    private void checkIfPhoneExists(String phone) {
        if (phone == null || phone.isEmpty()) {
            return;
        }

        userRepository.findByPhone(phone).ifPresent(user -> {
            if (user.getStatus().equals(UserStatus.INACTIVE)) {
                throw new BusinessException(
                        "The phone provided is already associated with a user who has been deleted.",
                        HttpStatus.CONFLICT
                );
            }
            throw new BusinessException(
                    "There is already a user with the phone " + phone,
                    HttpStatus.CONFLICT
            );
        });
    }

    @Override
    public UserDetails getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() ->
                new BusinessException("No user was found with the email provided", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public void deactivateUser(String email) {
        var user = (User) this.getUserByEmail(email);

        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new BusinessException(
                    "The user with the email provided is already inactive",
                    HttpStatus.BAD_REQUEST
            );
        }
        user.setStatus(UserStatus.INACTIVE);
        this.userRepository.save(user);
    }

    @Override
    public Token authenticateUser(LoginDto loginDto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        var user = (User) this.authenticationManager.authenticate(usernamePassword).getPrincipal();

        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new BusinessException(
                    "You are trying to access an inactive account",
                    HttpStatus.FORBIDDEN
            );
        }

        var token = this.tokenService.generateToken(user);
        return new Token(token);
    }

    @Override
    public List<User> getUsers(SearchUserDto userDto) {
        if (userDto.getEmail() == null){
            userDto.setEmail("");
        }
        if (userDto.getPhone() == null){
            userDto.setPhone("");
        }
        if (userDto.getName() == null){
            userDto.setName("");
        }
        if (userDto.getSurname() == null){
            userDto.setSurname("");
        }
        return userRepository.findByFilters(
                userDto.getId(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getPhone(),
                userDto.getEmail(),
                userDto.getStatus(),
                userDto.getAccess()
        );
    }

    @Override
    public User updateUser(UpdateUserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));

        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getSurname() != null) user.setSurname(userDto.getSurname());
        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getAccess() != null) user.setAccess(userDto.getAccess());
        if (userDto.getStatus() != null) user.setStatus(userDto.getStatus());

        userRepository.save(user);
        return user;
    }
}
