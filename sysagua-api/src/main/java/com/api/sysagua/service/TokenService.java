package com.api.sysagua.service;

import com.api.sysagua.model.User;

public interface TokenService {

    String generateToken(User user);

    String validateToken(String token);

    User getUserFromToken(String token);

    void invalidateToken(String token);

}
