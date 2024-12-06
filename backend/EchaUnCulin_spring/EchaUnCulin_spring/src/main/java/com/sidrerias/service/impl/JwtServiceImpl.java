package com.sidrerias.service.impl;

import com.sidrerias.dto.TokenResponse;
import com.sidrerias.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private final String secretToken;

    public JwtServiceImpl(@Value("${jwt.secret}") String secretToken) {
        this.secretToken = secretToken;
    }

    @Override
    public TokenResponse generateToken(Long userId) {
        Date expirationDate = new Date(Long.MAX_VALUE);
        String token= Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,this.secretToken)
                .compact();
        return  TokenResponse.builder()
                .accessToken(token).build();
    }

    @Override
    public Claims getClaims(String token) {
        return  Jwts.parser()
                .setSigningKey(this.secretToken)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isExpired(String token) {
        try{
            return getClaims(token).getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Integer extractUserId(String token) {
        try{
            return Integer.parseInt(getClaims(token).getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
