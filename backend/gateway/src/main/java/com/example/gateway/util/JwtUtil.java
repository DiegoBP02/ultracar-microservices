package com.example.gateway.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${token.issuer}")
    private String tokenIssuer;

    public void validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withIssuer(tokenIssuer)
                .build();
        verifier.verify(token);
    }
}
