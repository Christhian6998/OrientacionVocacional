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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class IntentoTest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idIntento;
	private Date fecha;
	private int numeroIntento;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	@JsonIgnore
	private Usuario usuario;
	
	@OneToMany(mappedBy = "intento")
	@JsonIgnore
    private List<Respuesta> respuestas;

    @OneToOne(mappedBy = "intento")
    @JsonIgnoreProperties("intento")
    private Recomendacion recomendacion;
}
