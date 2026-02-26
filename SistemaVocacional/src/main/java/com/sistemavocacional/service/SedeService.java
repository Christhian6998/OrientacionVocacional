package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Sede;
import com.sistemavocacional.repository.SedeRepository;

@Service
public class SedeService {
	@Autowired
    private SedeRepository sedeRepo;

    public Optional<Sede> buscarPorId(int idSede) {
        return sedeRepo.findById(idSede);
    }

    public List<Sede> listar() {
        return sedeRepo.findAll();
    }

    public List<Sede> listarPorInstitucion(int idInstitucion) {
        return sedeRepo.findByInstitucionIdInstitucionAndEstadoTrue(idInstitucion);
    }

    public Sede guardar(Sede s) {
        return sedeRepo.save(s);
    }

    public Sede actualizar(Sede s) {
        return sedeRepo.save(s);
    }

    public void eliminar(int idSede) {
        sedeRepo.deleteById(idSede);
    }
    
	public void cambiarEstado(int id){
        sedeRepo.findById(id).ifPresent(s -> {
            s.setEstado(!s.isEstado());
            sedeRepo.save(s);
        });
    }
}
