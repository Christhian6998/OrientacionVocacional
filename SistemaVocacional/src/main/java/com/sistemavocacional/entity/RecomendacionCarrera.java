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
public class RecomendacionCarrera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idRecCarrera;
	private double afinidad;
	private String razon;
	private boolean estado;
	
	@ManyToOne
    @JoinColumn(name = "id_recomendacion")
    private Recomendacion recomendacion;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;
}
