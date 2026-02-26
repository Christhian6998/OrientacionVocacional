package com.sistemavocacional.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Respuesta;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    List<Respuesta> findByPreguntaIdPregunta(Integer idPregunta);
    
    void deleteByPreguntaIdPregunta(Integer idPregunta);
    void deleteByIntentoIdIntento(Integer idIntento);
    void deleteByIntentoUsuarioIdUsuario(Integer idUsuario);
}