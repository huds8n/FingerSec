package service;

import java.io.Serializable;

import javax.inject.Inject;

import jpa.Transactional;
import model.Usuario;
import repository.Usuarios;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Usuarios cadastroUsuarioDAO;

	@Transactional
	public void salvar(Usuario cadastroUsuario) throws NegocioException {
		this.cadastroUsuarioDAO.salvar(cadastroUsuario);
	}

}
