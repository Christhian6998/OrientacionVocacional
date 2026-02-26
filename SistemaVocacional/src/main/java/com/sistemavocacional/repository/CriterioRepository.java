package com.sistemavocacional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Criterio;

@Repository
public interface CriterioRepository extends JpaRepository<Criterio, Integer> {
	Optional<Criterio> findByNombre(String nombre);
	
	List<Criterio> findAllByOrderByNombreAsc();
}
