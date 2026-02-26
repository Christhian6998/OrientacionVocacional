package com.sistemavocacional.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistemavocacional.entity.Usuario;
import com.sistemavocacional.repository.IntentoTestRepository;
import com.sistemavocacional.repository.RecomendacionCarreraRepository;
import com.sistemavocacional.repository.RecomendacionRepository;
import com.sistemavocacional.repository.RespuestaRepository;
import com.sistemavocacional.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuRepo;
	@Autowired
	private RecomendacionCarreraRepository recCarRepo;
	@Autowired
	private RecomendacionRepository recRepo;
	@Autowired
	private RespuestaRepository respuestaRepo;
	@Autowired
	private IntentoTestRepository intentoRepo;
	
	public Optional<Usuario> buscarPorId(int idUsuario){
		return usuRepo.findById(idUsuario);
	}
	
	public Usuario guardar(Usuario u) {
		return usuRepo.save(u);
	}

	public Optional<Usuario> buscarPorEmail(String email) {
		return usuRepo.findByEmail(email);
	}
	
	public Optional<Usuario> buscarPorTelefono(String telefono) {
		return usuRepo.findByTelefono(telefono);
	}

	public List<Usuario> listaUsuario() {
		return usuRepo.findAll();
	}

	public void actualizar(Usuario objU) {
		usuRepo.save(objU);
	}
	
	public List<Usuario> listarUsuariosConsentimiento(boolean consentimiento){
		return usuRepo.findByConsentimiento(consentimiento);
	}
	
	@Transactional
	public void eliminar(int idUsuario) {
	    if (!usuRepo.existsById(idUsuario)) {
	        throw new RuntimeException("El usuario no existe");
	    }
	    recCarRepo.deleteByRecomendacionIntentoUsuarioIdUsuario(idUsuario);
	    recRepo.deleteByIntentoUsuarioIdUsuario(idUsuario);
	    respuestaRepo.deleteByIntentoUsuarioIdUsuario(idUsuario);
	    intentoRepo.deleteByUsuarioIdUsuario(idUsuario);

	    usuRepo.deleteById(idUsuario);
	}
}
