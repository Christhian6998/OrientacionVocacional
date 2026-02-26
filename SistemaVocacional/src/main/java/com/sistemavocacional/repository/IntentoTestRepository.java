package com.sistemavocacional.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistemavocacional.entity.IntentoTest;

@Repository
public interface IntentoTestRepository extends JpaRepository<IntentoTest, Integer> {
    List<IntentoTest> findByUsuarioIdUsuario(Integer idUsuario);
    int countByUsuarioIdUsuario(int id);
    
    void deleteByUsuarioIdUsuario(Integer idUsuario);
}