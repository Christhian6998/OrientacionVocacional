package com.sistemavocacional.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.Pregunta;

import feign.Param;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {
	@Query(value = "SELECT * FROM pregunta WHERE fase = :fase AND id_criterio IN (:criterios) AND estado = true ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Pregunta> findRandomByFaseAndCriterio(@Param("fase") int fase, @Param("criterios") List<Integer> criterios);
	
	@Query(value = "SELECT * FROM pregunta WHERE fase = :fase AND estado = true ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<Pregunta> findRandomByFase(@Param("fase") int fase);
	
	List<Pregunta> findByFase(int fase);
	List<Pregunta> findAllByOrderByEnunciadoAsc();
	
	void deleteByCriterioIdCriterio(Integer idCriterio);
}
