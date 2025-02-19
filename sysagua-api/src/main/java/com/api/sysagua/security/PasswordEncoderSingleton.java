package com.api.sysagua.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderSingleton {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordEncoderSingleton() {
    }

    public static BCryptPasswordEncoder getEncoder() {
        return ENCODER;
    }
}
