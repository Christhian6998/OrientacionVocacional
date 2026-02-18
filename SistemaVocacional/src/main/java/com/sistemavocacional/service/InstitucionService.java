package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Institucion;
import com.sistemavocacional.repository.InstitucionRepository;
import com.sistemavocacional.repository.OfertaCarreraRepository;
import com.sistemavocacional.repository.SedeRepository;

import jakarta.transaction.Transactional;

@Service
public class InstitucionService {
	@Autowired
    private InstitucionRepository instRepo;
	
	@Autowired
	private OfertaCarreraRepository ofertaRepo;

	@Autowired
	private SedeRepository sedeRepo;

    public Optional<Institucion> buscarPorId(int idInstitucion) {
        return instRepo.findById(idInstitucion);
    }

    public Institucion guardar(Institucion i) {
        return instRepo.save(i);
    }

    public Optional<Institucion> buscarPorNombre(String nombre) {
        return instRepo.findByNombre(nombre);
    }

    public List<Institucion> listar() {
        return instRepo.findAll();
    }

    public List<Institucion> listarActivos() {
        return instRepo.findByEstadoTrue();
    }

    public void actualizar(Institucion i) {
        instRepo.save(i);
    }

    @Transactional
    public void eliminar(int idInstitucion) {
    	ofertaRepo.deleteByInstitucionIdInstitucion(idInstitucion);
        sedeRepo.deleteByInstitucionIdInstitucion(idInstitucion);
        
        instRepo.deleteById(idInstitucion);
    }
    
    public void cambiarEstado(int id){
        instRepo.findById(id).ifPresent(i -> {
            i.setEstado(!i.isEstado());
            instRepo.save(i);
        });
    }
}
