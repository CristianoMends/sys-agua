package com.api.sysagua.factory;

import com.api.sysagua.dto.user.CreateUserDto;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.model.User;
import com.api.sysagua.security.PasswordEncoderSingleton;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserFactory {
    private static final BCryptPasswordEncoder encoder = PasswordEncoderSingleton.getInstance().getEncoder();

    public static User createUser(CreateUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAccess(dto.getAccess());
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(encoder.encode(dto.getPassword()));

        return user;
    }
}
