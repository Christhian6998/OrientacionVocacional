package com.sistemavocacional.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistemavocacional.entity.Criterio;
import com.sistemavocacional.entity.CriterioCarrera;
import com.sistemavocacional.service.CriterioService;

@RestController
@RequestMapping("/criterio")
public class CriterioController {
	@Autowired
    private CriterioService criterioService;

    @GetMapping
    public List<Criterio> listar() {
        return criterioService.listar();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
    	Optional<Criterio> criterioOpt = criterioService.buscarPorId(id);

        if (criterioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Criterio no encontrado");
        }

        return ResponseEntity.ok(criterioOpt.get());
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody Criterio criterio) {
    	try {
            if (criterio.getNombre() == null || criterio.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del criterio es obligatorio");
            }
            String nombreUpper = criterio.getNombre().trim().toUpperCase();

            Optional<Criterio> existente =criterioService.buscarPorNombre(nombreUpper);

            if (existente.isPresent()) {
                return ResponseEntity.ok(existente.get());
            }

            criterio.setNombre(nombreUpper);

            Criterio nuevo = criterioService.guardar(criterio);

            return ResponseEntity.ok(nuevo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar criterio");
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Criterio criterio) {
    	Optional<Criterio> criterioOpt = criterioService.buscarPorId(id);
        if (criterioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Criterio no encontrado");
        }

        Criterio actual = criterioOpt.get();
        String nombreUpper = criterio.getNombre().trim().toUpperCase();
        Optional<Criterio> nombreExiste = criterioService.buscarPorNombre(nombreUpper);

        if (nombreExiste.isPresent() && nombreExiste.get().getIdCriterio() != id) {
            return ResponseEntity.badRequest().body("El nombre del criterio ya existe");
        }

        actual.setNombre(nombreUpper);

        Criterio actualizado = criterioService.actualizar(actual);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {
    	Optional<Criterio> criterioOpt = criterioService.buscarPorId(id);
        if (criterioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Criterio no encontrado");
        }
        criterioService.eliminarFisico(id);

        return ResponseEntity.ok("Criterio eliminado correctamente");
    }

    // criterioCarrera

    @GetMapping("buscarPorCarrera/{id}")
    public ResponseEntity<?> obtenerCarrerasPorCriterio(@PathVariable int id) {
    	return ResponseEntity.ok(criterioService.obtenerCarrerasPorCriterio(id));
    }
    
    @GetMapping("buscarPorCriterio/{id}")
    public ResponseEntity<?> obtenerCiterioPorCarrera(@PathVariable int id) {
    	return ResponseEntity.ok(criterioService.obtenerCriterioPorCarrera(id));
    }

    @PostMapping("/relacion")
    public ResponseEntity<?> guardarRelacion(@RequestBody CriterioCarrera cc) {
    	try {
            if (cc.getCriterio() == null || cc.getCarrera() == null || cc.getPeso()<=0) {
                return ResponseEntity.badRequest().body("Relación inválida");
            }
            CriterioCarrera nueva = criterioService.guardarRelacion(cc);
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar relación");
        }
    }
    
    @DeleteMapping("eliminarCritCarr/{id}")
    public ResponseEntity<?> eliminarCriterioCarrera(@PathVariable int id) {
    	Optional<CriterioCarrera> critCarrOpt = criterioService.buscarPorIdCritCarr(id);
        if (critCarrOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Criterio Carrera no encontrado");
        }
        criterioService.eliminarCriterioCarrera(id);

        return ResponseEntity.ok("Criterio Carrera eliminado correctamente");
    }
}
