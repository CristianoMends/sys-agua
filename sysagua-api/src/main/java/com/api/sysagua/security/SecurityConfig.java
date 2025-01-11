package com.api.sysagua.security;

import com.api.sysagua.enumeration.UserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    SecurityFilter securityFilter;
    private final String[] freeRoutes = {
            "/users/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api-docs/**",
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users").hasRole(UserAccess.DEVELOPER.name())//penas devs manipulam usuarios

                        .requestMatchers("/customers").authenticated()
                        .requestMatchers("/products").authenticated()
                        .requestMatchers(freeRoutes).permitAll()//rotas liberadas
                        .anyRequest().authenticated()//libera demais rotas pra usuario autenticados
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) //adiciona filtro antes, para checar o token
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}