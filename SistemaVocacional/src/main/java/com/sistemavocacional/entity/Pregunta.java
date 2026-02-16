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
public class Pregunta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPregunta;
	private String enunciado, tipo;
	private double peso;
	private boolean estado;
	
	@OneToMany(mappedBy = "pregunta")
	@JsonIgnore
    private List<Respuesta> respuestas;
}
