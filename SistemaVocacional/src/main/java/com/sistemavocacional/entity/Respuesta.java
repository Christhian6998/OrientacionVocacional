package com.sistemavocacional.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Respuesta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idRespuesta;
	// en vez de usuar IdUsuario y idOpcion y luego relacionarla, mejor la relacionamos 
	// directamente con la clase Usuario y Opcion
	
	private double afinidadPorcentaje;
	private String DescripcionResultado;
	
	@ManyToOne
	private Usuario usuario;
	
	@ManyToOne
	private Opcion opcion;
}
