package com.taskmanager.taskmanager.config.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.taskmanager.taskmanager.models.UserModel;

@Service
public class TokenService {
    
    @Value("${api.security.secret.token}")
    private String secret;

    public String generateToken(UserModel model) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                            .withIssuer("Task-Manager-API")
                            .withSubject(model.getEmail())
                            .withExpiresAt(generateExpireAt())
                            .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while authentication: GENERATE: " + e.getMessage(), e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Task-Manager-API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }   

    private Instant generateExpireAt() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }

}
