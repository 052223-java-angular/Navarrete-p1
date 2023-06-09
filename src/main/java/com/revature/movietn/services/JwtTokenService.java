package com.revature.movietn.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revature.movietn.dtos.responses.Principal;
import com.revature.movietn.utils.custom_exceptions.UnauthorizedAccessException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Generates a signed JWT.
     * 
     * @param principal the Principal DTO object with user info
     * @return the signed JWT
     */
    public String generateToken(Principal principal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", principal.getId());
        claims.put("role", principal.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // expires in 12 hours
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_16)))
                .compact();
    }

    /**
     * Validates a token by checking if the token is a valid compact Claims JWS
     * string (signed JWT with a Claims body), if JWS string was correctly
     * constructed, if it passes signature validation, if it is not an empty token,
     * and finally if the Claims match with the principal fields (user info
     * matches).
     * 
     * @param token     the signed JWS string
     * @param principal the Principal DTO object with user info
     */
    public void validateToken(String token, Principal principal) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_16)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!claims.getSubject().equals(principal.getUsername()) ||
                    !claims.get("id", String.class).equals(principal.getId()) ||
                    !claims.get("role", String.class).equals(principal.getRole())) {
                throw new UnauthorizedAccessException("Invalid Token");
            }

        } catch (ExpiredJwtException e) {
            throw new UnauthorizedAccessException("Expired token.");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException
                | IllegalArgumentException e) {
            throw new UnauthorizedAccessException("Invalid token.");
        }
    }
}
