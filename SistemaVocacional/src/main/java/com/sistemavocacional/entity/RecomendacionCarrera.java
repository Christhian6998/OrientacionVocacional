package com.sistemavocacional.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class RecomendacionCarrera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idRecCarrera;
	private double afinidad;
	
	@ManyToOne
    @JoinColumn(name = "id_recomendacion")
	@JsonIgnore
    private Recomendacion recomendacion;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;
}
