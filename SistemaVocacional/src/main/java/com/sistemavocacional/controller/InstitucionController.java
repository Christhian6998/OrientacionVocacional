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

import com.sistemavocacional.entity.Institucion;
import com.sistemavocacional.service.InstitucionService;

@RestController
@RequestMapping("/institucion")
public class InstitucionController {
	@Autowired
    private InstitucionService iSer;

    @PostMapping("/registrarInstitucion")
    public ResponseEntity<?> registrarInstitucion(@RequestBody Institucion i) {
        try {
        	String nombreUpper = i.getNombre().trim().toUpperCase();
            Optional<Institucion> existente = iSer.buscarPorNombre(nombreUpper);

            if (existente.isPresent()) {
                return ResponseEntity.ok(existente.get()); 
            }

            i.setNombre(i.getNombre().trim().toUpperCase());
            i.setTipo(i.getTipo().trim().toUpperCase());

            Institucion nueva = iSer.guardar(i);
            
            return ResponseEntity.ok(nueva);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la institución");
        }
    }

    @GetMapping("/listarInstituciones")
    public ResponseEntity<List<Institucion>> listarInstituciones() {
        return ResponseEntity.ok(iSer.listar());
    }

    @GetMapping("/listarInstitucionesActivas")
    public ResponseEntity<List<Institucion>> listarPorEstado() {
        return ResponseEntity.ok(iSer.listarActivos());
    }

    @PutMapping("/actualizarInstitucion/{id}")
    public ResponseEntity<?> actualizarInstitucion(@PathVariable int id, @RequestBody Institucion i) {

        Optional<Institucion> instOpt = iSer.buscarPorId(id);

        if (instOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Institución no encontrada");
        }

        Institucion actual = instOpt.get();

        Optional<Institucion> nombreExiste = iSer.buscarPorNombre(i.getNombre());
        if (nombreExiste.isPresent() && nombreExiste.get().getIdInstitucion() != id) {
            return ResponseEntity.badRequest().body("El nombre ya está en uso");
        }

        actual.setNombre(i.getNombre().trim().toUpperCase());
        actual.setTipo(i.getTipo().trim().toUpperCase());

        Institucion actualizada = iSer.actualizar(actual);
        
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/eliminarInstitucion/{id}")
    public ResponseEntity<?> eliminarInstitucion(@PathVariable int id) {

        Optional<Institucion> instOpt = iSer.buscarPorId(id);

        if (instOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Institución no encontrada");
        }

        iSer.eliminar(id);
        return ResponseEntity.ok("Institución eliminada correctamente");
    }
    
    @PatchMapping("/estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id){
        iSer.cambiarEstado(id);
        return ResponseEntity.ok("Estado de institución actualizado");
    }

}
