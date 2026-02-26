package com.sistemavocacional.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Recomendacion;
import com.sistemavocacional.repository.RecomendacionRepository;

@Service
public class RecomendacionService {

    @Autowired
    private RecomendacionRepository recomendacionRepo;

    public Optional<Recomendacion> buscarPorIdIntento(int id){
        return recomendacionRepo.findByIntentoIdIntento(id);
    }

}