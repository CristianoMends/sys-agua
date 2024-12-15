package com.api.sysagua.config;

import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.api.sysagua.model.User;
import com.api.sysagua.repository.UserRepository;

@Component
public class DefaultUserInitializer implements CommandLineRunner {
    @Value("${DEFAULT_EMAIL}")
    String defaultEmail;

    @Value("${DEFAULT_PASSWORD}")
    String defaultPassword;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByEmail(defaultEmail).isEmpty()) {

            User defaultUser = new User(
                    "Desenvolvedor",
                    null,
                    null,
                    defaultEmail,
                    passwordEncoder.encode(defaultPassword),
                    UserStatus.ACTIVE,
                    UserAccess.DEVELOPER
            );

            userRepository.save(defaultUser);
            System.out.println("Default User created successfully!");
        } else {
            System.out.println("Default user already exists");
        }
    }
}
