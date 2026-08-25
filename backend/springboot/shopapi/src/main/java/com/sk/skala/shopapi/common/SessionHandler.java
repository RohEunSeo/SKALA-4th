package com.sk.skala.shopapi.common;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sk.skala.shopapi.data.dto.CustomerSession;
import com.sk.skala.shopapi.exception.Error;
import com.sk.skala.shopapi.exception.ResponseException;
import com.sk.skala.shopapi.tools.StringUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class SessionHandler {

    private static final String BEARER_PREFIX = "Bearer ";

    private final Key key;
    private final long expirationMillis;

    public SessionHandler(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:3600000}") long expirationMillis) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    public String generateToken(CustomerSession session) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setSubject(String.valueOf(session.getCustomerId()))
                .claim("loginId", session.getLoginId())
                .claim("name", session.getName())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public CustomerSession resolve(String authorizationHeader) {
        if (StringUtil.isBlank(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new ResponseException(Error.UNAUTHORIZED);
        }
        return parseToken(authorizationHeader.substring(BEARER_PREFIX.length()));
    }

    public CustomerSession parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return CustomerSession.builder()
                    .customerId(Long.parseLong(claims.getSubject()))
                    .loginId(claims.get("loginId", String.class))
                    .name(claims.get("name", String.class))
                    .build();
        } catch (ExpiredJwtException e) {
            throw new ResponseException(Error.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseException(Error.INVALID_TOKEN);
        }
    }
}
