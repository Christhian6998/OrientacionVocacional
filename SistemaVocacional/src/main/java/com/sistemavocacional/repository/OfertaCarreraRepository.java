package com.sistemavocacional.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.OfertaCarrera;

@Repository
public interface OfertaCarreraRepository extends JpaRepository<OfertaCarrera, Integer> {
	List<OfertaCarrera> findByEstadoTrue();
	List<OfertaCarrera> findByCarreraIdCarreraAndEstadoTrue(Integer idCarrera);
    List<OfertaCarrera> findByInstitucionIdInstitucionAndEstadoTrue(Integer idInstitucion);
    
    void deleteByCarreraIdCarrera(Integer idCarrera);
    void deleteByInstitucionIdInstitucion(Integer idInstitucion);
}
