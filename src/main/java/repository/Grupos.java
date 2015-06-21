package repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import jpa.Transactional;
import model.Grupo;
import service.NegocioException;

public class Grupos implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public Grupo buscarPeloCodigo(Long id) {
		return manager.find(Grupo.class, id);
	}

	public void salvar(Grupo grupo) {
		manager.merge(grupo);
	}

	@SuppressWarnings("unchecked")
	public List<Grupo> buscarTodos() {
		return manager.createQuery("from Grupo").getResultList();
	}

	public Grupo peloNome(String nome) {
		return this.manager
				.createQuery("from Grupo where nome = :nome", Grupo.class)
				.setParameter("nome", nome).getSingleResult();
	}

	@Transactional
	public void excluir(Grupo grupo) throws NegocioException {
		grupo = buscarPeloCodigo(grupo.getId());
		try {
			manager.remove(grupo);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Modulo nao excluido");
		}
	}

}
