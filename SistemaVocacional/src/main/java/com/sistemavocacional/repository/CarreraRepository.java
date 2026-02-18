package com.sistemavocacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Carrera;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
	Optional<Carrera> findByNombre(String nombre);
	List<Carrera> findByEstadoTrue();
}
