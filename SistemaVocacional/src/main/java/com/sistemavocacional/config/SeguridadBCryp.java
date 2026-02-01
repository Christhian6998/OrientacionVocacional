package com.sistemavocacional.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sistemavocacional.entity.Usuario;
import com.sistemavocacional.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SeguridadBCryp {
	@Autowired
	private UsuarioService uSer;
	
	@Autowired
	HttpSession sesion;
	
	@Bean
	public UserDetailsService userDetailsService() {
	    return username -> {
	        Optional<Usuario> opUser = uSer.buscarPorEmail(username);
	        if (opUser.isEmpty()) {
	            throw new UsernameNotFoundException("Usuario no encontrado");
	        }

	        Usuario usu = opUser.get();
	        sesion.setAttribute("idusuario", usu.getIdUsuario());

	        /*String[] roles = usu.getListaTipoUsuario().stream()
	                .map(tipo -> tipo.getTipoUsuario().toUpperCase())
	                .toArray(String[]::new);*/

	        return org.springframework.security.core.userdetails
	                .User.builder()
	                .username(usu.getEmail())
	                .password(usu.getPassword())
	                //.roles(roles)
	                .build();
	    };
	}

    @Bean
    public BCryptPasswordEncoder getEncode() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(getEncode())
                .and()
                .build();
    }

	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                /*.requestMatchers("/venta/admin/**").hasRole("ADMIN")
                .requestMatchers("/venta/**").hasAnyRole("USER", "ADMIN")*/
                .anyRequest().permitAll()
                //.anyRequest().authenticated()

            )
            .httpBasic();

        return http.build();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("*")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }
}
