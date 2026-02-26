package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Pregunta;
import com.sistemavocacional.repository.PreguntaRepository;
import com.sistemavocacional.repository.RespuestaRepository;

import jakarta.transaction.Transactional;

@Service
public class PreguntaService {
    @Autowired
    private PreguntaRepository preguntaRepo;

    @Autowired
    private RespuestaRepository respuestaRepo;

    public Optional<Pregunta> buscarPorId(int idPregunta){
        return preguntaRepo.findById(idPregunta);
    }
    
    public List<Pregunta> buscarPorFase(int fase){
        return preguntaRepo.findByFase(fase);
    }

    public Pregunta guardar(Pregunta p){
        return preguntaRepo.save(p);
    }

    public List<Pregunta> listar(){
        return preguntaRepo.findAllByOrderByEnunciadoAsc();
    }

    public Pregunta actualizar(Pregunta p){
        return preguntaRepo.save(p);
    }

    @Transactional
    public void eliminar(int idPregunta){
        respuestaRepo.deleteByPreguntaIdPregunta(idPregunta);
        preguntaRepo.deleteById(idPregunta);
    }

    public void cambiarEstado(int id){
        preguntaRepo.findById(id).ifPresent(p -> {
            p.setEstado(!p.isEstado());
            preguntaRepo.save(p);
        });
    }
}
