package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import jpa.Transactional;
import model.Grupo;
import repository.Grupos;

public class GrupoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Grupos grupoDAO;

	@Transactional
	public void salvar(Grupo grupo) throws NegocioException {

		this.grupoDAO.salvar(grupo);
	}

	public List<Grupo> buscarTodos() {
		return grupoDAO.buscarTodos();
	}
	
	public Grupo peloCodigo(Long id){
		return this.grupoDAO.buscarPeloCodigo(id);
	}
	
	public Grupo peloNome(String id){
		return this.grupoDAO.peloNome(id);
	}

}
