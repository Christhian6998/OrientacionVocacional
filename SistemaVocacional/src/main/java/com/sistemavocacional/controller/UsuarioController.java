package com.sistemavocacional.controller;

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

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	private UsuarioService uSer;
	
	BCryptPasswordEncoder pasEncode = new BCryptPasswordEncoder();
	
	@PostMapping("/registrarUsuario")
	public ResponseEntity<?> guardarUsuario(@RequestBody Usuario u) {
	    try {
	        u.setPassword(pasEncode.encode(u.getPassword()));
	        
	        //Antes de guardar verificar que no haya repeticion de correo y telefono
	        //antes de guardar cambia los nombres y apellidos a mayusculas o a minuscula
	        // si el atributo "visible" esta vacio por default sera false
	        Usuario nuevo = uSer.guardar(u);

	        return ResponseEntity.ok(nuevo); // Devuelve 200 OK con el usuario registrado
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error al registrar usuario");
	    }
	}
	/*
	 * Crear metodos para eliminar
	 * crear metodo para actualizar, cuidando las mismas reglas de guardado
	 * crear metodo para listar (solo los usuarios con visibilidad TRUE)
	 * */
	
}
