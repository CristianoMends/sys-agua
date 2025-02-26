package com.api.sysagua.config;

import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.api.sysagua.model.User;
import com.api.sysagua.repository.UserRepository;

@Component
public class DefaultUserInitializer implements CommandLineRunner {

    @Value("${default-user.email}")
    String defaultEmail;

    @Value("${default-user.password}")
    String defaultPassword;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            User defaultUser = new User.Builder()
                    .withName("usuario")
                    .withEmail(defaultEmail)
                    .withPassword(defaultPassword)
                    .withStatus(UserStatus.ACTIVE)
                    .withAccess(UserAccess.OWNER)
                    .build();

            userRepository.save(defaultUser);
        }
    }
}
