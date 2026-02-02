package com.sistemavocacional.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemavocacional.entity.Usuario;
import com.sistemavocacional.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final LoginController loginController;
	@Autowired
	private UsuarioService uSer;
	
	BCryptPasswordEncoder pasEncode = new BCryptPasswordEncoder();

    UsuarioController(LoginController loginController) {
        this.loginController = loginController;
    }
	
	@PostMapping("/registrarUsuario")
	public ResponseEntity<?> guardarUsuario(@RequestBody Usuario u) {
	    try {
	        u.setPassword(pasEncode.encode(u.getPassword()));
	        
	        if (!uSer.buscarPorEmail(u.getEmail()).isEmpty()) {
	            return ResponseEntity
	                    .status(HttpStatus.BAD_REQUEST)
	                    .body("El correo ya está registrado");
	        }
	        if (!uSer.buscarPorTelefono(u.getTelefono()).isEmpty()) {
	            return ResponseEntity
	                    .status(HttpStatus.BAD_REQUEST)
	                    .body("El teléfono ya está registrado");
	        }
	        
	        u.setNombre(u.getNombre().trim().toUpperCase());
	        u.setApellido(u.getApellido().trim().toUpperCase());
	        
	        u.setRol("POSTULANTE");
	        
	        Usuario nuevo = uSer.guardar(u);

	        return ResponseEntity.ok(nuevo); // Devuelve 200 OK con el usuario registrado
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error al registrar usuario");
	    }
	}


	@GetMapping("/listarUsuariosConsentimiento")
	public ResponseEntity<List<Usuario>> listarUsuariosConsentimiento() {
	    return ResponseEntity.ok(
	            uSer.listarUsuariosConsentimiento(true)
	    );
	}
	
	@GetMapping("/listarUsuarios")
	public ResponseEntity<List<Usuario>> listarUsuarios() {
	    return ResponseEntity.ok(uSer.listaUsuario());
	}
	
	
	@PutMapping("/actualizarUsuario/{id}")
	public ResponseEntity<?> actualizarUsuario(@PathVariable int id, @RequestBody Usuario u) {

	    Optional<Usuario> usuarioOpt = uSer.buscarPorId(id);

	    if (usuarioOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Usuario no encontrado");
	    }

	    Usuario actual = usuarioOpt.get();

	    // Validar correo
	    Optional<Usuario> correoExiste = uSer.buscarPorEmail(u.getEmail());
	    if (correoExiste.isPresent() && correoExiste.get().getIdUsuario() != id) {
	        return ResponseEntity.badRequest().body("El correo ya está en uso");
	    }

	    // Validar teléfono
	    Optional<Usuario> telefonoExiste = uSer.buscarPorTelefono(u.getTelefono());
	    if (telefonoExiste.isPresent() && telefonoExiste.get().getIdUsuario() != id) {
	        return ResponseEntity.badRequest().body("El teléfono ya está en uso");
	    }

	    // Actualizar datos
	    actual.setNombre(u.getNombre().toUpperCase());
	    actual.setApellido(u.getApellido().toUpperCase());
	    actual.setEmail(u.getEmail());
	    actual.setTelefono(u.getTelefono());

	    // Actualizar consentimiento si llega
	    if (u.isConsentimiento() != actual.isConsentimiento()) {
	        actual.setConsentimiento(u.isConsentimiento());
	    }

	    // Actualizar contraseña solo si se envía
	    if (u.getPassword() != null && !u.getPassword().isEmpty()) {
	        actual.setPassword(pasEncode.encode(u.getPassword()));
	    }

	    uSer.actualizar(actual);
	    return ResponseEntity.ok("Usuario actualizado correctamente");
	}

	@DeleteMapping("/eliminarUsuario/{id}")
	public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {

	    Optional<Usuario> usuarioOpt = uSer.buscarPorId(id);

	    if (usuarioOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Usuario no encontrado");
	    }

	    uSer.eliminar(id);
	    return ResponseEntity.ok("Usuario eliminado correctamente");
	}


	
}
