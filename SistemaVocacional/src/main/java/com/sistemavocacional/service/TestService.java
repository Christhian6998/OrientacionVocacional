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
        final double MAX_PUNTAJE_POR_PREGUNTA = 3.0;

        // 1. Setup inicial
        Usuario user = userRepo.findById(dto.getIdUsuario()).orElseThrow(() -> new RuntimeException("User not found"));
        IntentoTest intento = new IntentoTest();
        intento.setUsuario(user);
        intento.setFecha(new Date());
        intento.setNumeroIntento(intentoRepo.countByUsuarioIdUsuario(user.getIdUsuario()) + 1);
        IntentoTest guardado = intentoRepo.save(intento);

        Map<Integer, Double> puntajePorCriterio = new HashMap<>();
        Map<Integer, Double> maxPosiblePorCriterio = new HashMap<>();

        // 2. Procesar respuestas y parsear áreas
        List<Respuesta> listaRespuestas = dto.getRespuestas().stream().map(rDto -> {
            Respuesta r = new Respuesta();
            r.setIntento(guardado);
            Pregunta p = preguntaRepo.findById(rDto.getIdPregunta()).orElseThrow();
            r.setPregunta(p);
            r.setValor(rDto.getValor());
            r.setPuntaje(rDto.getPuntaje());

            List<Integer> idsCriterios = new ArrayList<>();
            if (p.getCriterio() != null) {
                idsCriterios.add(p.getCriterio().getIdCriterio());
            } else if (p.getArea() != null) {
                String[] nombres = p.getArea().split("_");
                for (String nombre : nombres) {
                    criterioRep.findByNombre(nombre).ifPresent(c -> idsCriterios.add(c.getIdCriterio()));
                }
            }

            double puntajeUsuarioEnPregunta = rDto.getPuntaje() * p.getPeso();
            double maxPosibleEnPregunta = MAX_PUNTAJE_POR_PREGUNTA * p.getPeso();

            for (Integer idCriterio : idsCriterios) {
                puntajePorCriterio.put(idCriterio, puntajePorCriterio.getOrDefault(idCriterio, 0.0) + puntajeUsuarioEnPregunta);
                maxPosiblePorCriterio.put(idCriterio, maxPosiblePorCriterio.getOrDefault(idCriterio, 0.0) + maxPosibleEnPregunta);
            }
            return r;
        }).toList();
        
        respuestaRepo.saveAll(listaRespuestas);

        // 3. Recomendación base
        List<Integer> criteriosTop = puntajePorCriterio.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey).toList();

        Recomendacion rec = new Recomendacion();
        rec.setPerfil(criteriosTop.stream().limit(2).map(id -> {
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
            double puntosObtenidos = 0.0;
            double puntosMaximosEvaluados = 0.0;
            int criteriosCoincidentes = 0;

            for (CriterioCarrera cc : requisitos) {
                Integer idCrit = cc.getCriterio().getIdCriterio();
                
                if (maxPosiblePorCriterio.containsKey(idCrit)) {
                    double peso = cc.getPeso();
                    double puntajeUsuario = puntajePorCriterio.get(idCrit);
                    double maxCriterio = maxPosiblePorCriterio.get(idCrit);

                    double ratio = puntajeUsuario / maxCriterio;
                    
                    double bono = (peso >= 7) ? 1.2 : 1.0;

                    puntosObtenidos += (ratio * peso * bono);
                    puntosMaximosEvaluados += (peso * bono);
                    criteriosCoincidentes++;
                }
            }

            double factorPenalizacion = (criteriosCoincidentes < 2) ? 0.7 : 1.0;

            double porcentaje = (puntosMaximosEvaluados > 0) 
                ? (puntosObtenidos / puntosMaximosEvaluados) * 100 * factorPenalizacion
                : 5.0;

            afinidadCarrera.put(idCarrera, Math.min(porcentaje, 99.0)); 
        });

        // 5. Build del listado final
        List<RecomendacionCarrera> listaFinal = new ArrayList<>();
        
        // Agregar Top 3 generadas
        List<Integer> carrerasTop = afinidadCarrera.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(3).map(Map.Entry::getKey).toList();

        for (Integer idCarrera : carrerasTop) {
            Carrera c = carreraRepo.findById(idCarrera).orElseThrow();
            listaFinal.add(crearRecCarrera(recGuardada, c, afinidadCarrera.get(idCarrera)));
        }

        // 6. Force de carreras manuales
        Arrays.asList(idC1, idC2, idC3).forEach(id -> {
            if (id != null && id > 0) {
                carreraRepo.findById(id).ifPresent(c -> {
                    if (listaFinal.stream().noneMatch(lf -> lf.getCarrera().getIdCarrera().equals(c.getIdCarrera()))) {
                        listaFinal.add(crearRecCarrera(recGuardada, c, afinidadCarrera.getOrDefault(c.getIdCarrera(), 5.0)));
                    }
                });
            }
        });

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