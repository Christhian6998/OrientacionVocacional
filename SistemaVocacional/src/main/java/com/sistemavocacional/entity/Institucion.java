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
public class Institucion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idInstitucion;
	private String nombre, tipo;
	private boolean estado;

    @OneToMany(mappedBy = "institucion")
    @JsonIgnore
    private List<OfertaCarrera> ofertas;

    @OneToMany(mappedBy = "institucion")
    @JsonIgnore
    private List<Sede> sedes;
}
