package com.sistemavocacional.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Carrera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCarrera;
	private String nombre, area;
	
	@Column(columnDefinition = "TEXT")
	private String descripcion;
	private boolean estado;

    @OneToMany(mappedBy = "carrera")
    @JsonIgnore
    private List<OfertaCarrera> ofertas;

    @OneToMany(mappedBy = "carrera")
    @JsonIgnore
    private List<RecomendacionCarrera> recomendaciones;
	
}
