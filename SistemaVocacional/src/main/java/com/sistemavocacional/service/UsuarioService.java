package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Usuario;
import com.sistemavocacional.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuRepo;
	
	public Optional<Usuario> buscarPorId(int idUsuario){
		return usuRepo.findById(idUsuario);
	}
	
	public Usuario guardar(Usuario u) {
		return usuRepo.save(u);
	}

	public Optional<Usuario> buscarPorEmail(String email) {
		return usuRepo.findByEmail(email);
	}

	public List<Usuario> listaUsuario() {
		return usuRepo.findAll();
	}

	public void actualizar(Usuario objU) {
		usuRepo.save(objU);
	}
	
	public List<Usuario> listarUsuariosVisibles(boolean visible){
		return usuRepo.findByVisible(visible);
	}
}
