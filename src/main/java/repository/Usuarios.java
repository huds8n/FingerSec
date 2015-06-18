package repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import jpa.Transactional;
import model.Usuario;
import service.NegocioException;

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Usuario porId(Long id) {
		return this.manager.find(Usuario.class, id);
	}

	public List<Usuario> vendedores() {
		// TODO filtrar apenas vendedores (por um grupo específico)
		return this.manager.createQuery("from Usuario", Usuario.class)
				.getResultList();
	}

	@Transactional
	public void salvar(Usuario cadastroUsuario) throws NegocioException {
		this.manager.merge(cadastroUsuario);
	}

	public Usuario porLogin(String login) {
		Usuario usuario = null;

		try {
			usuario = this.manager
					.createQuery("from Usuario where lower(login) = :login",
							Usuario.class)
					.setParameter("login", login.toLowerCase())
					.getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}

		return usuario;
	}

}