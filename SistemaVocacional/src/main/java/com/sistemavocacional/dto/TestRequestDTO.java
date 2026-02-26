package com.sistemavocacional.dto;

import java.util.List;

import lombok.Data;

@Data
public class TestRequestDTO {
    private Integer idUsuario;
    private List<RespuestaItemDTO> respuestas;
}