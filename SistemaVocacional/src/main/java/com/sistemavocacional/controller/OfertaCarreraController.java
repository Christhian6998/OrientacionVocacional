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

import com.sistemavocacional.entity.OfertaCarrera;
import com.sistemavocacional.service.CarreraService;
import com.sistemavocacional.service.InstitucionService;
import com.sistemavocacional.service.OfertaCarreraService;

@RestController
@RequestMapping("/ofertaCarrera")
public class OfertaCarreraController {
	@Autowired
    private OfertaCarreraService ofSer;

    @Autowired
    private CarreraService cSer;

    @Autowired
    private InstitucionService iSer;

    @PostMapping("/registrarOferta")
    public ResponseEntity<?> registrarOferta(@RequestBody OfertaCarrera of) {

        if (of.getCarrera() == null || of.getCarrera().getIdCarrera() == null) {
            return ResponseEntity.badRequest().body("Debe seleccionar una carrera");
        }

        if (cSer.buscarPorId(of.getCarrera().getIdCarrera()).isEmpty()) {
            return ResponseEntity.badRequest().body("La carrera no existe");
        }

        if (of.getInstitucion() == null || of.getInstitucion().getIdInstitucion() == null) {
            return ResponseEntity.badRequest().body("Debe seleccionar una institución");
        }

        if (iSer.buscarPorId(of.getInstitucion().getIdInstitucion()).isEmpty()) {
            return ResponseEntity.badRequest().body("La institución no existe");
        }

        of.setModalidad(of.getModalidad().trim().toUpperCase());

        OfertaCarrera nueva = ofSer.guardar(of);
        return ResponseEntity.ok(nueva);
    }


    @GetMapping("/listarOfertas")
    public ResponseEntity<List<OfertaCarrera>> listarOfertas() {
        return ResponseEntity.ok(ofSer.listar());
    }

    @GetMapping("/listarOfertasCarrera/{idCarrera}")
    public ResponseEntity<List<OfertaCarrera>> listarPorCarrera(@PathVariable int idCarrera){
        return ResponseEntity.ok(ofSer.listarPorCarrera(idCarrera));
    }

    @GetMapping("/listarOfertasInstitucion/{idInstitucion}")
    public ResponseEntity<List<OfertaCarrera>> listarPorInstitucion(@PathVariable int idInstitucion){
        return ResponseEntity.ok(ofSer.listarPorInstitucion(idInstitucion));
    }

    @PutMapping("/actualizarOferta/{id}")
    public ResponseEntity<?> actualizarOferta(@PathVariable int id, @RequestBody OfertaCarrera of) {

        Optional<OfertaCarrera> ofertaOpt = ofSer.buscarPorId(id);

        if (ofertaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Oferta no encontrada");
        }

        OfertaCarrera actual = ofertaOpt.get();

        if (of.getCarrera() != null && of.getCarrera().getIdCarrera() != null) {
            if (cSer.buscarPorId(of.getCarrera().getIdCarrera()).isEmpty()) {
                return ResponseEntity.badRequest().body("La carrera no existe");
            }
            actual.setCarrera(of.getCarrera());
        }

        if (of.getInstitucion() != null && of.getInstitucion().getIdInstitucion() != null) {
            if (iSer.buscarPorId(of.getInstitucion().getIdInstitucion()).isEmpty()) {
                return ResponseEntity.badRequest().body("La institución no existe");
            }
            actual.setInstitucion(of.getInstitucion());
        }

        // Actualizar datos
        actual.setDuracion(of.getDuracion());
        actual.setCostoMatricula(of.getCostoMatricula());
        actual.setCostoPension(of.getCostoPension());
        actual.setModalidad(of.getModalidad().trim().toUpperCase());

        OfertaCarrera ofertaActual=ofSer.actualizar(actual);

        return ResponseEntity.ok(ofertaActual);
    }

    @DeleteMapping("/eliminarOferta/{id}")
    public ResponseEntity<?> eliminarOferta(@PathVariable int id) {

        Optional<OfertaCarrera> ofertaOpt = ofSer.buscarPorId(id);

        if (ofertaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Oferta no encontrada");
        }

        ofSer.eliminar(id);
        
        return ResponseEntity.ok("Oferta eliminada correctamente");
    }
    
    @PatchMapping("/estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id){
        ofSer.cambiarEstado(id);
        return ResponseEntity.ok("Estado de oferta actualizado");
    }
    

}

