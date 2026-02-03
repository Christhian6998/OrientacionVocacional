package com.sistemavocacional.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.sistemavocacional.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	private final String SECRET_KEY = "Desarr0lloDePruebaVocacional.";

    // tiempo de vida del token (30 minutos)
    private final long EXPIRATION_TIME = 1000 * 60 * 30;

    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("rol", usuario.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
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
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
