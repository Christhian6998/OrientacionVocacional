package com.sistemavocacional.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.CriterioCarrera;

@Repository
public interface CriterioCarreraRepository extends JpaRepository<CriterioCarrera, Integer> {
	List<CriterioCarrera> findByCriterioIdCriterio(Integer idCriterio);
	List<CriterioCarrera> findByCarreraIdCarrera(Integer idCarrera);
	List<CriterioCarrera> findByCriterioIdCriterioIn(List<Integer> ids);
	
	void deleteByCarreraIdCarrera(Integer idCarrera);
	void deleteByCriterioIdCriterio(Integer idCriterio);
}
