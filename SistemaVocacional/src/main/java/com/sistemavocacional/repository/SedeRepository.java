package com.sistemavocacional.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Sede;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
	List<Sede> findByEstadoTrue();
	List<Sede> findByInstitucionIdInstitucionAndEstadoTrue(Integer idInstitucion);

	void deleteByInstitucionIdInstitucion(Integer idInstitucion);
}
