package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Carrera;
import com.sistemavocacional.repository.CarreraRepository;
import com.sistemavocacional.repository.CriterioCarreraRepository;
import com.sistemavocacional.repository.OfertaCarreraRepository;

import jakarta.transaction.Transactional;

@Service
public class CarreraService {
	@Autowired
    private CarreraRepository carreraRep;
	
	@Autowired
	private OfertaCarreraRepository ofertaRep;
	
	@Autowired
	private CriterioCarreraRepository criCarrRep;

    public Optional<Carrera> buscarPorId(int id){
        return carreraRep.findById(id);
    }

    public Carrera guardar(Carrera c){
        return carreraRep.save(c);
    }
    
    public Optional<Carrera> buscarPorNombre(String nombre){
        return carreraRep.findByNombre(nombre);
    }

    public List<Carrera> listar(){
        return carreraRep.findAll();
    }
    
    public List<Carrera> listarActivas(){
        return carreraRep.findByEstadoTrueOrderByNombreAsc();
    }


    public Carrera actualizar(Carrera c){
        return carreraRep.save(c);
    }

    @Transactional
    public void eliminarFisico(int id){
    	ofertaRep.deleteByCarreraIdCarrera(id);
    	criCarrRep.deleteByCarreraIdCarrera(id);;
    	
        carreraRep.deleteById(id);
    }

    public void cambiarEstado(int id){
        carreraRep.findById(id).ifPresent(c -> {
            c.setEstado(!c.isEstado());
            carreraRep.save(c);
        });
    }

}
