package com.sistemavocacional.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemavocacional.entity.Usuario;
import com.sistemavocacional.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private UsuarioService uSer;
	
	BCryptPasswordEncoder pasEncode = new BCryptPasswordEncoder();
	
	@PostMapping("/acceder")
	public ResponseEntity<?> acceder(@RequestBody Usuario u, HttpSession sesion) {
	    Optional<Usuario> opUser = uSer.buscarPorEmail(u.getEmail());

	    if (opUser.isEmpty()) {
	        Map<String, Object> error = new HashMap<>();
	        error.put("mensaje", "Usuario no encontrado");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	    }

	    Usuario user = opUser.get();

	    if (!pasEncode.matches(u.getPassword(), user.getPassword())) {
	        Map<String, Object> error = new HashMap<>();
	        error.put("mensaje", "Credenciales incorrectas");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	    }

	    // Guardar en sesión (para pruebas)
	    sesion.setAttribute("idusuario", user.getIdUsuario());

	    Map<String, Object> respuesta = new HashMap<>();
	    respuesta.put("mensaje", "Acceso concedido");
	    respuesta.put("idUsuario", user.getIdUsuario());

	    return ResponseEntity.ok(respuesta);
	}
}
