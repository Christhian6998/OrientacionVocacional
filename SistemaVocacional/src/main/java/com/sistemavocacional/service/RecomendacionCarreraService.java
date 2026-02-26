package com.sistemavocacional.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.RecomendacionCarrera;
import com.sistemavocacional.repository.RecomendacionCarreraRepository;


@Service
public class RecomendacionCarreraService {
    @Autowired
    private RecomendacionCarreraRepository recCarreraRepo;

    public List<RecomendacionCarrera> buscarPorIdRecomendacion(Integer id){
        return recCarreraRepo.findByRecomendacionIdRecomendacion(id);
    }
}
