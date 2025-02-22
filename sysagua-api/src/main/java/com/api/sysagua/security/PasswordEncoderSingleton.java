package com.api.sysagua.security;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
public class PasswordEncoderSingleton {
    private static PasswordEncoderSingleton instance;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private PasswordEncoderSingleton() {
    }
    public static PasswordEncoderSingleton getInstance(){
        if (instance == null) instance = new PasswordEncoderSingleton();

        return instance;
    }
}
