package com.Auth.JWT.config;

import java.security.Key;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET_KEY = "1714438ba8b7d8031cb5e0465dbf7a464b5502441d29d42596eb78107d162e50";

    public String extractUserName(String token){
        return null;
    }

    private Key getSignIngKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extarctAllClaims(String token){
        return Jwts.parserBuilder()
                    .setSigningKey(getSignIngKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    public <T> T extarctClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extarctAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
}
