package com.sistemavocacional.dto;

import lombok.Data;

@Data
public class RespuestaItemDTO {
    private Integer idPregunta;
    private String valor;
    private double puntaje;
}