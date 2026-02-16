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
public class OfertaCarrera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idOferta;
	private int duracion;
	private double costoMatricula, costoPension;
	private String modalidad;
	private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;

    @ManyToOne
    @JoinColumn(name = "id_institucion")
    private Institucion institucion;
}
