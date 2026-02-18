package com.sistemavocacional.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemavocacional.entity.Carrera;
import com.sistemavocacional.service.CarreraService;

@RestController
@RequestMapping("/carrera")
public class CarreraController {
	@Autowired
    private CarreraService cSer;

	@PostMapping("/registrarCarrera")
	public ResponseEntity<?> registrarCarrera(@RequestBody Carrera c) {
	    try {
	        if (c.getNombre() == null) return ResponseEntity.badRequest().body("Nombre requerido");

	        String nombreUpper = c.getNombre().trim().toUpperCase();
	        Optional<Carrera> existente = cSer.buscarPorNombre(nombreUpper);

	        if (existente.isPresent()) {
	            return ResponseEntity.ok(existente.get());
	        }

	        c.setNombre(nombreUpper);
	        c.setDescripcion(c.getDescripcion() != null ? c.getDescripcion().trim() : "");
	        c.setArea(c.getArea() != null ? c.getArea().trim().toUpperCase() : "GENERAL");
	        c.setEstado(true);

	        Carrera nueva = cSer.guardar(c);
	        return ResponseEntity.ok(nueva);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
	    }
	}


    @GetMapping("/listarCarreras")
    public ResponseEntity<List<Carrera>> listarCarreras() {
        return ResponseEntity.ok(cSer.listar());
    }


    @GetMapping("/listarCarrerasActivas")
    public ResponseEntity<List<Carrera>> listarPorEstado() {
        return ResponseEntity.ok(cSer.listarActivas());
    }


    @PutMapping("/actualizarCarrera/{id}")
    public ResponseEntity<?> actualizarCarrera(@PathVariable int id, @RequestBody Carrera c) {

        Optional<Carrera> carreraOpt = cSer.buscarPorId(id);

        if (carreraOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Carrera no encontrada");
        }

        Carrera actual = carreraOpt.get();

        Optional<Carrera> nombreExiste = cSer.buscarPorNombre(c.getNombre());
        if (nombreExiste.isPresent() && nombreExiste.get().getIdCarrera() != id) {
            return ResponseEntity.badRequest().body("El nombre ya está en uso");
        }

        actual.setNombre(c.getNombre().trim().toUpperCase());
        actual.setDescripcion(c.getDescripcion());
        actual.setArea(c.getArea().toUpperCase());

        cSer.actualizar(actual);

        return ResponseEntity.ok("Carrera actualizada correctamente");
    }

    @DeleteMapping("/eliminarCarrera/{id}")
    public ResponseEntity<?> eliminarCarrera(@PathVariable int id) {

        Optional<Carrera> carreraOpt = cSer.buscarPorId(id);

        if (carreraOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Carrera no encontrada");
        }

        cSer.eliminarFisico(id);
        return ResponseEntity.ok("Carrera eliminada correctamente");
    }
    
    @PatchMapping("/estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id){
        cSer.cambiarEstado(id);
        return ResponseEntity.ok("Estado de carrera actualizado");
    }
	
}
