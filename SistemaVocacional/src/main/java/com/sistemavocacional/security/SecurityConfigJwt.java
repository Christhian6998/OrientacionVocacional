package com.sistemavocacional.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfigJwt {
	@Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	http
    	.cors(cors -> {})
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login").permitAll()
            .requestMatchers("/usuario/registrarUsuario").permitAll()
            .requestMatchers("/usuario/listarUsuariosConsentimiento").hasRole("ADMIN")
            .requestMatchers("/usuario/listarUsuarios").hasRole("ADMIN")
            .requestMatchers("/usuario/eliminarUsuario/**").hasRole("ADMIN")
            .requestMatchers("/usuario/actualizarUsuario/**").hasAnyRole("ADMIN", "POSTULANTE")
            
            .requestMatchers("/test/**").hasRole("POSTULANTE")
            
            .requestMatchers("/pregunta/**").hasRole("ADMIN")
            
            .requestMatchers("/criterio/**").hasRole("ADMIN")
            
            .requestMatchers("/institucion/listarInstitucionesActivas").permitAll()
            .requestMatchers("/institucion/listarInstituciones").hasRole("ADMIN")
            .requestMatchers("/institucion/registrarInstitucion").hasRole("ADMIN")
            .requestMatchers("/institucion/actualizarInstitucion/**").hasRole("ADMIN")
            .requestMatchers("/institucion/eliminarInstitucion/**").hasRole("ADMIN")
            .requestMatchers("/institucion/estado/**").hasRole("ADMIN")
            
            .requestMatchers("/sede/listarSedesInstitucion/**").permitAll()
            .requestMatchers("/sede/listarSedes").hasRole("ADMIN")
            .requestMatchers("/sede/registrarSede").hasRole("ADMIN")
            .requestMatchers("/sede/actualizarSede/**").hasRole("ADMIN")
            .requestMatchers("/sede/eliminarSede/**").hasRole("ADMIN")
            .requestMatchers("/sede/estado/**").hasRole("ADMIN")
            
            .requestMatchers("/carrera/listarCarrerasActivas").permitAll()
            .requestMatchers("/carrera/listarCarreras").hasRole("ADMIN")
            .requestMatchers("/carrera/registrarCarrera").hasRole("ADMIN")
            .requestMatchers("/carrera/actualizarCarrera/**").hasRole("ADMIN")
            .requestMatchers("/carrera/eliminarCarrera/**").hasRole("ADMIN")
            .requestMatchers("/carrera/estado/**").hasRole("ADMIN")
            
            .requestMatchers("/ofertaCarrera/listarOfertasCarrera/**").permitAll()
            .requestMatchers("/ofertaCarrera/listarOfertasInstitucion/**").permitAll()
            .requestMatchers("/ofertaCarrera/listarOfertas").hasRole("ADMIN")
            .requestMatchers("/ofertaCarrera/registrarOferta").hasRole("ADMIN")
            .requestMatchers("/ofertaCarrera/actualizarOferta/**").hasRole("ADMIN")
            .requestMatchers("/ofertaCarrera/eliminarOferta/**").hasRole("ADMIN")
            .requestMatchers("/ofertaCarrera/estado/**").hasRole("ADMIN")
            //.requestMatchers("/usuario/actualizarUsuario/**").hasAnyRole("ADMIN", "POSTULANTE")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
