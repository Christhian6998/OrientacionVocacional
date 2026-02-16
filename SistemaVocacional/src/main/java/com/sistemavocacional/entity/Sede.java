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
public class Sede {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idSede;
	private String nombre, direccion;
	private double longitud, latitud;
	private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_institucion")
    private Institucion institucion;
}
