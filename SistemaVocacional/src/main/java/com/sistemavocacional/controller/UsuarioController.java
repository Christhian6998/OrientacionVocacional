package com.sistemavocacional.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@Autowired
	private UsuarioService uSer;
	
	BCryptPasswordEncoder pasEncode = new BCryptPasswordEncoder();

	
	@PostMapping("/registrarUsuario")
	public ResponseEntity<?> guardarUsuario(@RequestBody Usuario u) {
	    try {
	        String errorValidacion = validarInputs(u);
	        if (errorValidacion != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorValidacion);
            }

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

	        return ResponseEntity.ok(nuevo);
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
	
	@GetMapping("/buscarPorEmail/{email}")
	public ResponseEntity<Optional<Usuario>> buscarUsuario(@PathVariable String email) {
	    return ResponseEntity.ok(uSer.buscarPorEmail(email));
	}
	
	
	@PutMapping("/actualizarUsuario/{id}")
	public ResponseEntity<?> actualizarUsuario(@PathVariable int id, @RequestBody Usuario u) {
		// validacion de seguridad
		String errorValidacion = validarInputs(u);
        if (errorValidacion != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorValidacion);
        }
        
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String emailAutenticado = auth.getName();
	    boolean isAdmin = auth.getAuthorities().stream()
	                          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

	    Optional<Usuario> usuarioOpt = uSer.buscarPorId(id);

	    if (usuarioOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Usuario no encontrado");
	    }

	    Usuario actual = usuarioOpt.get();

	    if (!isAdmin && !actual.getEmail().equals(emailAutenticado)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                             .body("No tienes permiso para actualizar este perfil");
	    }
	    
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
	    actual.setDireccion(u.getDireccion());
	    actual.setFechaNacimiento(u.getFechaNacimiento());

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

	private String validarInputs(Usuario u) {
        String letrasEspacios = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+$";
        String alfanumericoEspacios = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s]+$";
        String telefonoRegex = "^9\\d{8}$";

        if (u.getNombre() == null || !u.getNombre().matches(letrasEspacios)) {
            return "Nombre inválido: solo letras y espacios.";
        }
        if (u.getApellido() == null || !u.getApellido().matches(letrasEspacios)) {
            return "Apellido inválido: solo letras y espacios.";
        }
        if (u.getTelefono() == null || !u.getTelefono().matches(telefonoRegex)) {
            return "Teléfono inválido: debe tener 9 dígitos y empezar con 9.";
        }
        if (u.getDireccion() != null && !u.getDireccion().trim().isEmpty()) {
            if (!u.getDireccion().matches(alfanumericoEspacios)) {
                return "Dirección inválida: solo letras, números y espacios.";
            }
        }
        if (u.getFechaNacimiento() == null) {
            return "La fecha de nacimiento es obligatoria.";
        }
        
        LocalDate fechaNac = u.getFechaNacimiento().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
		LocalDate hoy = LocalDate.now();
		int edad = Period.between(fechaNac, hoy).getYears();
		
		if (edad < 14 || edad > 25) {
		return "Edad no permitida: debe tener entre 14 y 25 años (Edad actual: " + edad + ").";
		}
        
        return null;
    }

	
}
