package com.interview.roiti.gradingsystem.service.impl;

import com.interview.roiti.gradingsystem.model.User;
import com.interview.roiti.gradingsystem.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    private final String JWT_SECRET = "XVC9mTU2hvJif5F7Q4S2p6NN3HkwdFpp";


    @Override
    public String generateToken(User user) {
        Instant expirationTime = Instant.now().plus(1, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationTime);

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        String compactTokenString = Jwts.builder()
                .claim("id", user.getId())
                .claim("sub", user.getUser())
                .claim("admin", user.isAdmin())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + compactTokenString;
    }

    @Override
    public User parseToken(String token) {
        byte[] secretBytes = JWT_SECRET.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);

        String username = jwsClaims.getBody().getSubject();
        Long id = jwsClaims.getBody().get("id", Long.class);
        boolean isAdmin = jwsClaims.getBody().get("admin", Boolean.class);

        return new User(username, id, isAdmin);
    }
}
