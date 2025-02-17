package com.api.sysagua.security;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderSingleton {
    @Getter
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private PasswordEncoderSingleton() {}
}