package com.sistemavocacional.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"id_criterio","id_carrera"})
	    }
	)
@Data
public class CriterioCarrera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCritCarrera;
	private int peso;
	
	@ManyToOne
    @JoinColumn(name = "id_criterio")
    private Criterio criterio;
	
	@ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera;
	
}
