package com.sistemavocacional.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	private String token;
	private String rol;
    private String email;
    private Integer id;
	
}
