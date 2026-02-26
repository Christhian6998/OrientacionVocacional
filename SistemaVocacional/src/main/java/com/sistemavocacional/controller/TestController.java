package com.sistemavocacional.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistemavocacional.dto.TestRequestDTO;
import com.sistemavocacional.entity.Pregunta;
import com.sistemavocacional.entity.RecomendacionCarrera;
import com.sistemavocacional.repository.CriterioRepository;
import com.sistemavocacional.service.IntentoTestService;
import com.sistemavocacional.service.RecomendacionCarreraService;
import com.sistemavocacional.service.RecomendacionService;
import com.sistemavocacional.service.TestService;


@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
    private TestService testService;
	@Autowired
	private RecomendacionCarreraService recCarService;
	@Autowired
	private RecomendacionService recService;
	@Autowired
	private IntentoTestService intentoService;
	@Autowired
	private CriterioRepository criterioRepo;

	@PostMapping("/guardar")
	public ResponseEntity<List<RecomendacionCarrera>> guardarTest(
	    @RequestBody TestRequestDTO dto,
	    @RequestParam(required = false, defaultValue = "0") Integer idC1,
	    @RequestParam(required = false, defaultValue = "0") Integer idC2,
	    @RequestParam(required = false, defaultValue = "0") Integer idC3
	) {
	    return ResponseEntity.ok(testService.registrarResultadoManual(dto, idC1, idC2, idC3));
	}
	
	@GetMapping("/fase/{numeroFase}")
	public ResponseEntity<List<Pregunta>> obtenerPreguntas(
	        @PathVariable int numeroFase, 
	        @RequestParam(required = false) List<Integer> criterios) {
		return ResponseEntity.ok(testService.obtenerPreguntasPorFase(numeroFase, criterios));
	}
	
	@GetMapping("/recomendacion-carrera/{idRecomendacion}")
    public ResponseEntity<?> buscarRecCarrera(@PathVariable Integer idRecomendacion) {
        return ResponseEntity.ok(recCarService.buscarPorIdRecomendacion(idRecomendacion));
    }

    @GetMapping("/recomendacion/intento/{idIntento}")
    public ResponseEntity<?> buscarRecomendacionPorIntento(@PathVariable Integer idIntento) {
        return ResponseEntity.ok(recService.buscarPorIdIntento(idIntento));
    }

    @GetMapping("/intentos/usuario/{idUsuario}")
    public ResponseEntity<?> buscarIntentosPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(intentoService.listarPorUsuario(idUsuario));
    }
    
    @GetMapping("/buscar-id/{nombre}")
    public ResponseEntity<Integer> obtenerIdPorNombre(@PathVariable String nombre) {
        return criterioRepo.findByNombre(nombre)
                .map(c -> ResponseEntity.ok(c.getIdCriterio()))
                .orElse(ResponseEntity.notFound().build());
    }

}
