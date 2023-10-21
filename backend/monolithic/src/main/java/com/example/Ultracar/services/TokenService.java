package com.example.Ultracar.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.Ultracar.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${token.expiration}")
    private long tokenExpiration;
    @Value("${timezone.offset}")
    private String timezoneOffSet;
    @Value("${token.issuer}")
    private String tokenIssuer;

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer(tokenIssuer)
                .withSubject(user.getName())
                .withClaim("id", user.getId().toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(Date.from(LocalDateTime.now()
                        .plusMinutes(tokenExpiration)
                        .toInstant(ZoneOffset.of(timezoneOffSet))))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .withIssuer(tokenIssuer)
                .build().verify(token).getSubject();
    }
}
