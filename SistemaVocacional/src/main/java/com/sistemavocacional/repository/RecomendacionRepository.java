package com.sistemavocacional.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Recomendacion;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Integer> {
    Optional<Recomendacion> findByIntentoIdIntento(Integer idIntento);

    void deleteByIntentoIdIntento(Integer idIntento);
    void deleteByIntentoUsuarioIdUsuario(Integer idUsuario);
}
