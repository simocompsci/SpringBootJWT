package com.Auth.JWT.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET_KEY = "1714438ba8b7d8031cb5e0465dbf7a464b5502441d29d42596eb78107d162e50";

    public String extractUserName(String token) {
        return extarctClaim(token, Claims::getSubject);
    }

    private Key getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extarctAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignIngKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extarctClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extarctAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String generateToken(UserDetails  usersDetails){
        return generateToken(new HashMap<>() , usersDetails);

    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {
               return Jwts.builder()
               .setClaims(extraClaims)
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
               .signWith(getSignIngKey() , SignatureAlgorithm.HS256)
               .compact();
    }

    public Boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUserName(token);
        return (username == userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extarctClaim(token, Claims::getExpiration);
    }

}
