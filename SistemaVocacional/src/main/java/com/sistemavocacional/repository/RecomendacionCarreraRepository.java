package com.sistemavocacional.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.RecomendacionCarrera;

@Repository
public interface RecomendacionCarreraRepository extends JpaRepository<RecomendacionCarrera, Integer> {
    List<RecomendacionCarrera> findByRecomendacionIdRecomendacion(Integer idRecomendacion);

    void deleteByRecomendacionIdRecomendacion(Integer idRecomendacion);
    void deleteByCarreraIdCarrera(Integer idCarrera);
    void deleteByRecomendacionIntentoUsuarioIdUsuario(Integer idUsuario);
}
