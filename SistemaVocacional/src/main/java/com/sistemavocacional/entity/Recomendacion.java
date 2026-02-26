package com.sistemavocacional.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Recomendacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idRecomendacion;
	private String perfil;
	private Date fechaRegistrada;

    @OneToOne
    @JoinColumn(name = "id_intento")
    @JsonIgnoreProperties("recomendacion")
    private IntentoTest intento;

    @OneToMany(mappedBy = "recomendacion")
    @JsonIgnore
    private List<RecomendacionCarrera> carreras;
}
