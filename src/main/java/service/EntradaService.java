package service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import jpa.Transactional;
import model.Entrada;
import repository.Entradas;

public class EntradaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Entradas entradas;

	@Transactional
	public Entrada salvar(Entrada empresa) throws NegocioException {
		return entradas.guardar(empresa);
	}

	public Entrada porID(Long id) throws NegocioException {
		return entradas.porId(id);
	}
	
	public List<Entrada> listarTodas() throws NegocioException {
		return entradas.listarTodas();
	}
	
	public List<Entrada> listarPorFuncionario(Long id) throws NegocioException {
		return entradas.porFuncionario(id);
	}
	
	public List<Entrada> listarPorData(Date data) throws NegocioException {
		return entradas.porData(data);
	}
	
	public List<Entrada> listarPorDataAtivo(Date data) throws NegocioException {
		return entradas.porDataAtivos(data);
	}

}
