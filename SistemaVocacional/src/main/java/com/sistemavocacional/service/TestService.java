package com.sistemavocacional.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.dto.TestRequestDTO;
import com.sistemavocacional.entity.Carrera;
import com.sistemavocacional.entity.Criterio;
import com.sistemavocacional.entity.CriterioCarrera;
import com.sistemavocacional.entity.IntentoTest;
import com.sistemavocacional.entity.Pregunta;
import com.sistemavocacional.entity.Recomendacion;
import com.sistemavocacional.entity.RecomendacionCarrera;
import com.sistemavocacional.entity.Respuesta;
import com.sistemavocacional.entity.Usuario;
import com.sistemavocacional.repository.CarreraRepository;
import com.sistemavocacional.repository.CriterioCarreraRepository;
import com.sistemavocacional.repository.CriterioRepository;
import com.sistemavocacional.repository.IntentoTestRepository;
import com.sistemavocacional.repository.PreguntaRepository;
import com.sistemavocacional.repository.RecomendacionCarreraRepository;
import com.sistemavocacional.repository.RecomendacionRepository;
import com.sistemavocacional.repository.RespuestaRepository;
import com.sistemavocacional.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class TestService {
    @Autowired
    private IntentoTestRepository intentoRepo;
    @Autowired
    private RespuestaRepository respuestaRepo;
    @Autowired
    private UsuarioRepository userRepo;
    @Autowired
    private PreguntaRepository preguntaRepo;
    @Autowired
    private RecomendacionRepository recomendacionRepo;
    @Autowired
    private RecomendacionCarreraRepository recCarreraRepo;
    @Autowired 
    private CarreraRepository carreraRepo;
    @Autowired
    private CriterioCarreraRepository criCarrRep;
    @Autowired
    private CriterioRepository criterioRep;

    public List<Pregunta> obtenerPreguntasPorFase(int fase, List<Integer> criterios) {
        if (fase == 1 || criterios == null || criterios.isEmpty()) {
            return preguntaRepo.findRandomByFase(fase);
        }
        return preguntaRepo.findRandomByFaseAndCriterio(fase, criterios);
    }

    @Transactional
    public List<RecomendacionCarrera> registrarResultadoManual(TestRequestDTO dto, Integer idC1, Integer idC2, Integer idC3) {
        
        // 1. Setup inicial
        Usuario user = userRepo.findById(dto.getIdUsuario()).orElseThrow(() -> new RuntimeException("User not found"));
        IntentoTest intento = new IntentoTest();
        intento.setUsuario(user);
        intento.setFecha(new Date());
        intento.setNumeroIntento(intentoRepo.countByUsuarioIdUsuario(user.getIdUsuario()) + 1);
        IntentoTest guardado = intentoRepo.save(intento);

        Map<Integer, Double> puntajePorCriterio = new HashMap<>();

        // 2. Procesar respuestas y parsear áreas
        List<Respuesta> listaRespuestas = dto.getRespuestas().stream().map(rDto -> {
            Respuesta r = new Respuesta();
            r.setIntento(guardado);
            Pregunta p = preguntaRepo.findById(rDto.getIdPregunta()).orElseThrow();
            r.setPregunta(p);
            r.setValor(rDto.getValor());
            r.setPuntaje(rDto.getPuntaje());

            double puntajeUsuarioEnPregunta = rDto.getPuntaje() * p.getPeso();

            if (p.getCriterio() != null) {
                puntajePorCriterio.merge(p.getCriterio().getIdCriterio(), puntajeUsuarioEnPregunta, Double::sum);
            } else if (p.getArea() != null) {
                for (String nombre : p.getArea().split("_")) {
                    criterioRep.findByNombre(nombre).ifPresent(c -> 
                        puntajePorCriterio.merge(c.getIdCriterio(), puntajeUsuarioEnPregunta, Double::sum)
                    );
                }
            }
            return r;
        }).toList();
        
        respuestaRepo.saveAll(listaRespuestas);

        // 3. Recomendación base
        List<Integer> userTop5 = puntajePorCriterio.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .limit(5) // Limitar estrictamente a 5
                .toList();

        Recomendacion rec = new Recomendacion();
        rec.setPerfil(userTop5.stream().limit(2).map(id -> {
			        	Criterio c = criterioRep.findById(id).orElse(null);
			            return (c != null) ? c.getNombre() : "Unknown";
			        }).collect(Collectors.joining(" - ")));
        rec.setFechaRegistrada(new Date());
        rec.setIntento(guardado);
        Recomendacion recGuardada = recomendacionRepo.save(rec);

        // 4. Match de afinidad flexible (Core Logic)
        Map<Integer, Double> afinidadCarrera = new HashMap<>();
        Map<Integer, List<CriterioCarrera>> relacionesPorCarrera = criCarrRep.findAll().stream()
                .collect(Collectors.groupingBy(cc -> cc.getCarrera().getIdCarrera()));

        relacionesPorCarrera.forEach((idCarrera, requisitos) -> {
        	List<Integer> careerTop5 = requisitos.stream()
                    .sorted((a, b) -> Integer.compare(b.getPeso(), a.getPeso()))
                    .map(cc -> cc.getCriterio().getIdCriterio())
                    .toList();
        	
        	double afinidad = 0.0;
            double[] pesosAf = {40.0, 30.0, 15.0, 10.0, 5.0};

            for (int i = 0; i < careerTop5.size() && i < 5; i++) {
                Integer idCritCarrera = careerTop5.get(i);
                
                if (i < userTop5.size() && idCritCarrera.equals(userTop5.get(i))) {
                    afinidad += pesosAf[i];
                } else if (userTop5.contains(idCritCarrera)) {
                    afinidad += pesosAf[i] * 0.5; 
                }
            }

            afinidad += (idCarrera * 0.00001);
            afinidadCarrera.put(idCarrera, Math.min(afinidad, 99.99));
        });

        // 5. Build del listado final
        List<RecomendacionCarrera> listaFinal = new ArrayList<>();
        
        // Agregar Top 3 generadas
        List<Integer> idManuales = java.util.stream.Stream.of(idC1, idC2, idC3)
                .filter(id -> id != null && id > 0)
                .distinct()
                .toList();
        // Camino A: Insertar manuales con su afinidad real calculada
        for (Integer idM : idManuales) {
            carreraRepo.findById(idM).ifPresent(c -> {
                listaFinal.add(crearRecCarrera(recGuardada, c, afinidadCarrera.getOrDefault(idM, 0.0)));
            });
        }
        
        // Camino B: Completar con el ranking automático hasta llegar a 3
        List<Integer> mejoresCalculadas = afinidadCarrera.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        for (Integer idAuto : mejoresCalculadas) {
            if (listaFinal.size() >= 3) break;
            
            if (listaFinal.stream().noneMatch(rc -> rc.getCarrera().getIdCarrera().equals(idAuto))) {
                Carrera c = carreraRepo.findById(idAuto).orElseThrow();
                listaFinal.add(crearRecCarrera(recGuardada, c, afinidadCarrera.get(idAuto)));
            }
        }

        return recCarreraRepo.saveAll(listaFinal);
    }

    private RecomendacionCarrera crearRecCarrera(Recomendacion rec, Carrera car, double afinidad) {
        RecomendacionCarrera rc = new RecomendacionCarrera();
        rc.setAfinidad(Math.round(afinidad * 100.0) / 100.0);
        rc.setRecomendacion(rec);
        rc.setCarrera(car);
        return rc;
    }
    
}