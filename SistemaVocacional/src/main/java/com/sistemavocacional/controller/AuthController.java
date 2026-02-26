package com.sistemavocacional.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistemavocacional.dto.LoginRequest;
import com.sistemavocacional.dto.LoginResponse;
import com.sistemavocacional.entity.Usuario;
import com.sistemavocacional.security.JwtUtil;
import com.sistemavocacional.service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UsuarioService uSer;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	BCryptPasswordEncoder pasEncode = new BCryptPasswordEncoder();
	
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {

        Usuario user = uSer.buscarPorEmail(login.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Usuario no encontrado"));

        if (!pasEncode.matches(login.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas");
        }

        String token = jwtUtil.generarToken(user);

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        user.getRol(),
                        user.getEmail(),
                        user.getIdUsuario()
                )
        );
    }
}
