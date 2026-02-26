package com.sistemavocacional.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Respuesta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idRespuesta;
	private String valor;
	private double puntaje;
	
	@ManyToOne
    @JoinColumn(name = "id_pregunta")
    private Pregunta pregunta;

    @ManyToOne
    @JoinColumn(name = "id_intento")
    private IntentoTest intento;
}
