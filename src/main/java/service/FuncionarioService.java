package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import jpa.Transactional;
import repository.Funcionarios;
import model.Funcionario;

public class FuncionarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Funcionarios funcionarios;

	@Transactional
	public Funcionario salvar(Funcionario funcionario) throws NegocioException {
		return funcionarios.guardar(funcionario);
	}

	public Funcionario porID(Long id) throws NegocioException {
		return funcionarios.porId(id);
	}
	
	public List<Funcionario> buscarTodos() throws NegocioException {
		return funcionarios.todos();
	}
	
	public List<Funcionario> porNome(String str) throws NegocioException {
		return funcionarios.porNome(str);
	}
}
