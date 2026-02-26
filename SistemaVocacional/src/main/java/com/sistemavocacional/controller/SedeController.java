package com.sistemavocacional.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.sistemavocacional.entity.Sede;
import com.sistemavocacional.service.InstitucionService;
import com.sistemavocacional.service.SedeService;

@RestController
@RequestMapping("/sede")
public class SedeController {
	@Autowired
    private SedeService sSer;

    @Autowired
    private InstitucionService iSer;

    @PostMapping("/registrarSede")
    public ResponseEntity<?> registrarSede(@RequestBody Sede s) {

        if (s.getInstitucion() == null || s.getInstitucion().getIdInstitucion() == null) {
            return ResponseEntity.badRequest().body("Debe seleccionar una institución");
        }

        if (iSer.buscarPorId(s.getInstitucion().getIdInstitucion()).isEmpty()) {
            return ResponseEntity.badRequest().body("La institución no existe");
        }

        s.setNombre(s.getNombre().trim().toUpperCase());
        s.setDireccion(s.getDireccion().trim());

        Sede nueva = sSer.guardar(s);
        
        return ResponseEntity.ok(nueva);
    }

    @GetMapping("/listarSedes")
    public ResponseEntity<List<Sede>> listarSedes() {
        return ResponseEntity.ok(sSer.listar());
    }

    @GetMapping("/listarSedesInstitucion/{idInstitucion}")
    public ResponseEntity<List<Sede>> listarPorInstitucion(@PathVariable int idInstitucion) {
        return ResponseEntity.ok(sSer.listarPorInstitucion(idInstitucion));
    }

    @PutMapping("/actualizarSede/{id}")
    public ResponseEntity<?> actualizarSede(@PathVariable int id, @RequestBody Sede s) {

        Optional<Sede> sedeOpt = sSer.buscarPorId(id);

        if (sedeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Sede no encontrada");
        }

        Sede actual = sedeOpt.get();

        if (s.getInstitucion() != null && s.getInstitucion().getIdInstitucion() != null) {
            if (iSer.buscarPorId(s.getInstitucion().getIdInstitucion()).isEmpty()) {
                return ResponseEntity.badRequest().body("La institución no existe");
            }
            actual.setInstitucion(s.getInstitucion());
        }

        // Actualizar campos
        actual.setNombre(s.getNombre().trim().toUpperCase());
        actual.setDireccion(s.getDireccion().trim());
        actual.setLatitud(s.getLatitud());
        actual.setLongitud(s.getLongitud());

        Sede sedeActual=sSer.actualizar(actual);
        
        return ResponseEntity.ok(sedeActual);
    }


    @DeleteMapping("/eliminarSede/{id}")
    public ResponseEntity<?> eliminarSede(@PathVariable int id) {

        Optional<Sede> sedeOpt = sSer.buscarPorId(id);

        if (sedeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Sede no encontrada");
        }

        sSer.eliminar(id);
        
        return ResponseEntity.ok("Sede eliminada correctamente");
    }
    
    @PatchMapping("/estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id){
        sSer.cambiarEstado(id);
        return ResponseEntity.ok("Estado de sede actualizado");
    }

}
