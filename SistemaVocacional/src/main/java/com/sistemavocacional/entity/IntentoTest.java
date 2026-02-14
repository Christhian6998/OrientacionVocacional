package com.sistemavocacional.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
	private Usuario usuario;
}
