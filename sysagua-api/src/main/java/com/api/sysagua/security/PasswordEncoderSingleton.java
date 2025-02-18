package com.api.sysagua.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderSingleton {

    private static BCryptPasswordEncoder encoder;

    private PasswordEncoderSingleton() {
    }

    public static BCryptPasswordEncoder getEncoder() {
        if (encoder == null) encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}