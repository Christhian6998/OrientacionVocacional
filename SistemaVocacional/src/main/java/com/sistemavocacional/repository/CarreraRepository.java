package com.sistemavocacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Carrera;

import feign.Param;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
	Optional<Carrera> findByNombre(String nombre);
	List<Carrera> findByEstadoTrueOrderByNombreAsc();
	
	@Query("SELECT c FROM Carrera c WHERE (LOWER(c.area) = LOWER(:tipo) OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :tipo, '%'))) AND c.estado = true")
    List<Carrera> findCandidatas(@Param("tipo") String tipo);
}
