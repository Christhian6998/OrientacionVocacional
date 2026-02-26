package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Criterio;
import com.sistemavocacional.entity.CriterioCarrera;
import com.sistemavocacional.repository.CriterioCarreraRepository;
import com.sistemavocacional.repository.CriterioRepository;
import com.sistemavocacional.repository.PreguntaRepository;

import jakarta.transaction.Transactional;

@Service
public class CriterioService {
	@Autowired
    private CriterioRepository criterioRep;

    @Autowired
    private CriterioCarreraRepository criCarrRep;
    
    @Autowired PreguntaRepository preRep;

    // crud criterio

    public Optional<Criterio> buscarPorId(int id){
        return criterioRep.findById(id);
    }
    
    public Optional<Criterio> buscarPorNombre(String nombre){
        return criterioRep.findByNombre(nombre);
    }

    public List<Criterio> listar(){
        return criterioRep.findAllByOrderByNombreAsc();
    }

    public Criterio guardar(Criterio c){
        return criterioRep.save(c);
    }

    public Criterio actualizar(Criterio c){
        return criterioRep.save(c);
    }

    @Transactional
    public void eliminarFisico(int id){
        criCarrRep.deleteByCriterioIdCriterio(id);
        preRep.deleteByCriterioIdCriterio(id);
        criterioRep.deleteById(id);
    }

    // crud criterio carrera

    public List<CriterioCarrera> obtenerCarrerasPorCriterio(int idCriterio){
        return criCarrRep.findByCriterioIdCriterio(idCriterio);
    }
    
    public List<CriterioCarrera> obtenerCriterioPorCarrera(int idCarrera){
        return criCarrRep.findByCarreraIdCarrera(idCarrera);
    }

    public CriterioCarrera guardarRelacion(CriterioCarrera cc){
        return criCarrRep.save(cc);
    }
    public void eliminarCriterioCarrera(int id) {
    	criCarrRep.deleteById(id);
    }
    
    public Optional<CriterioCarrera> buscarPorIdCritCarr(int id){
        return criCarrRep.findById(id);
    }
}
