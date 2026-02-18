package com.sistemavocacional.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.sistemavocacional.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private final Key SECRET_KEY =
            Keys.hmacShaKeyFor(
                "Desarrollo_De_Prueba_Vocacional_super_segura_123456789."
                .getBytes(StandardCharsets.UTF_8)
            );

    // tiempo de vida del token (30 minutos)
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("rol", usuario.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String obtenerEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    public boolean tokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
    	return Jwts.parserBuilder()
    	        .setSigningKey(SECRET_KEY)
    	        .build()
    	        .parseClaimsJws(token)
    	        .getBody();
    }
}
