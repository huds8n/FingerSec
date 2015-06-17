package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import jpa.Transactional;
import repository.Empresas;
import model.Empresa;

public class EmpresaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Empresas empresas;

	@Transactional
	public Empresa salvar(Empresa empresa) throws NegocioException {
		return empresas.guardar(empresa);
	}

	public Empresa porID(Long id) throws NegocioException {
		return empresas.porId(id);
	}
	
	public List<Empresa> listarTodas() throws NegocioException {
		return empresas.listarTodas();
	}

}
