package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.OfertaCarrera;
import com.sistemavocacional.repository.OfertaCarreraRepository;

@Service
public class OfertaCarreraService {
	@Autowired
    private OfertaCarreraRepository ofertaRepo;

    public Optional<OfertaCarrera> buscarPorId(int idOferta) {
        return ofertaRepo.findById(idOferta);
    }

    public OfertaCarrera guardar(OfertaCarrera o) {
        return ofertaRepo.save(o);
    }

    public List<OfertaCarrera> listar() {
        return ofertaRepo.findAll();
    }

    public List<OfertaCarrera> listarPorCarrera(int idCarrera){
        return ofertaRepo.findByCarreraIdCarreraAndEstadoTrue(idCarrera);
    }

    public List<OfertaCarrera> listarPorInstitucion(int idInstitucion){
        return ofertaRepo.findByInstitucionIdInstitucionAndEstadoTrue(idInstitucion);
    }

    public OfertaCarrera actualizar(OfertaCarrera o) {
        return ofertaRepo.save(o);
    }

    public void eliminar(int idOferta) {
        ofertaRepo.deleteById(idOferta);
    }
    
    public void cambiarEstado(int id){
        ofertaRepo.findById(id).ifPresent(o -> {
            o.setEstado(!o.isEstado());
            ofertaRepo.save(o);
        });
    }
}
