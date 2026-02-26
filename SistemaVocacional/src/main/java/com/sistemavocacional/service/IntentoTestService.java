package com.sistemavocacional.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.IntentoTest;
import com.sistemavocacional.repository.IntentoTestRepository;
import com.sistemavocacional.repository.RespuestaRepository;

import jakarta.transaction.Transactional;

@Service
public class IntentoTestService {
    @Autowired
    private IntentoTestRepository intentoRepo;

    @Autowired
    private RespuestaRepository respuestaRepo;

    public List<IntentoTest> listarPorUsuario(int idUsuario){
        return intentoRepo.findByUsuarioIdUsuario(idUsuario);
    }

    @Transactional
    public void eliminar(int idIntento){
        respuestaRepo.deleteByIntentoIdIntento(idIntento);
        intentoRepo.deleteById(idIntento);
    }
}
