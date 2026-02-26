package com.sistemavocacional.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Criterio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCriterio;
	private String nombre;
	
	@OneToMany(mappedBy = "criterio")
    @JsonIgnore
    private List<CriterioCarrera> criterioCarrera;
	
	@OneToMany(mappedBy = "criterio")
    @JsonIgnore
    private List<Pregunta> preguntas;
}
