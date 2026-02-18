package com.sistemavocacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Institucion;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, Integer> {
	Optional<Institucion> findByNombre(String nombre);
	List<Institucion> findByEstadoTrue();
}
