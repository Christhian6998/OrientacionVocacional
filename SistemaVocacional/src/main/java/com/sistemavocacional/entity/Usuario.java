package com.sistemavocacional.entity;

import java.util.Date;
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
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUsuario;
	private String nombre, apellido, email, direccion, telefono, password, rol;
	private boolean consentimiento;
	private Date fechaNacimiento, fechaRegistro;
	
	// Recomendacion cuando hagas de onetomany coloca el jsonignore
	@OneToMany(mappedBy = "usuario")
	@JsonIgnore
	private List<Respuesta> respuesta;

}
