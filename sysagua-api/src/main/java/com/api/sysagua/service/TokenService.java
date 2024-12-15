package com.api.sysagua.service;

import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenService {

    @Value("${security.config.jwt-secret-key}")
    private String secretKey;
    @Value("${security.config.jwt-expiration-time}")
    private int expireTime;
    private final Set<String> revokedTokens = new HashSet<>();

    private boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }

    public void revokeToken(String token) {
        if (!isTokenRevoked(token)) {
            revokedTokens.add(token);
        }
    }

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("sysagua-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new BusinessException("Error while generating token", exception);
        }
    }
    public String validateToken(String token){
        try{
            if (isTokenRevoked(token)) {
                throw new BusinessException("Token has been revoked");
            }
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("sysagua-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(expireTime).toInstant(ZoneOffset.of("-03:00"));
    }
}
