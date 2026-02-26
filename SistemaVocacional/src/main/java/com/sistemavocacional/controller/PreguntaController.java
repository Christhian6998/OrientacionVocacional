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

import com.sistemavocacional.entity.Criterio;
import com.sistemavocacional.entity.Pregunta;
import com.sistemavocacional.service.CriterioService;
import com.sistemavocacional.service.PreguntaService;

@RestController
@RequestMapping("/pregunta")
public class PreguntaController {
	@Autowired
    private PreguntaService pSer;
	@Autowired
	private CriterioService cSer;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Pregunta p) {
        try {
            if (p.getEnunciado() == null || p.getEnunciado().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El campo no puede estar vacío");
            }
            if(p.getPeso()<=0 || p.getFase()<=0) {
            	return ResponseEntity.badRequest().body("Datos invalidos");
            }
            
            if (p.getFase() == 1) {
                p.setCriterio(null);
            } else {
                if (p.getCriterio() == null || p.getCriterio().getIdCriterio() == null) {
                    return ResponseEntity.badRequest().body("Preguntas de fase 2, 3 o 4 requieren un criterio");
                }
                Optional<Criterio> critOpt = cSer.buscarPorId(p.getCriterio().getIdCriterio());
                if (critOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Criterio no encontrado");
                }
                p.setCriterio(critOpt.get());
            }

            p.setEnunciado(p.getEnunciado().trim().toUpperCase());
            p.setEstado(true);

            Pregunta nueva = pSer.guardar(p);
            return ResponseEntity.ok(nueva);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la pregunta");
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Pregunta>> listar() {
        return ResponseEntity.ok(pSer.listar());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {
        Optional<Pregunta> pregOpt = pSer.buscarPorId(id);
        if (pregOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Pregunta no encontrada");
        }
        return ResponseEntity.ok(pregOpt.get());
    }
    
    @GetMapping("/buscarFase/{fase}")
    public ResponseEntity<?> buscarPorFase(@PathVariable int fase) {
        List<Pregunta> preguntas = pSer.buscarPorFase(fase);
        return ResponseEntity.ok(preguntas);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Pregunta p) {
        Optional<Pregunta> pregOpt = pSer.buscarPorId(id);

        if (pregOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Pregunta no encontrada");
        }
        
        Pregunta actual = pregOpt.get();
        
        if (p.getEnunciado() != null && !p.getEnunciado().isEmpty()) {
            actual.setEnunciado(p.getEnunciado().trim().toUpperCase());
        }

        if (p.getArea() != null && !p.getArea().isEmpty()) {
            actual.setArea(p.getArea());
        }
        
        if(p.getPeso()>0) {
        	actual.setPeso(p.getPeso());
        }
        
        if(p.getFase()>0) {
        	actual.setFase(p.getFase());
        }
        
        if (actual.getFase() == 1) {
            actual.setCriterio(null);
        } else {
            if (p.getCriterio() != null && p.getCriterio().getIdCriterio() != null) {
                Optional<Criterio> critOpt = cSer.buscarPorId(p.getCriterio().getIdCriterio());
                if(critOpt.isPresent()){
                    actual.setCriterio(critOpt.get());
                } else {
                    return ResponseEntity.badRequest().body("Criterio no encontrado");
                }
            } else if (actual.getCriterio() == null) {
                return ResponseEntity.badRequest().body("Preguntas de fase 2, 3 o 4 requieren un criterio");
            }
        }

        Pregunta preguntaActual=pSer.actualizar(actual);

        return ResponseEntity.ok(preguntaActual);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable int id) {

        Optional<Pregunta> pregOpt = pSer.buscarPorId(id);

        if (pregOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Pregunta no encontrada");
        }

        pSer.eliminar(id);

        return ResponseEntity.ok("Pregunta eliminada correctamente");
    }
    
    @PatchMapping("/estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id){
        pSer.cambiarEstado(id);
        return ResponseEntity.ok("Estado de pregunta actualizado");
    }
}
