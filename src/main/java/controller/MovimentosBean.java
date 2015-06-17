package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Entrada;
import service.EntradaService;
import service.NegocioException;

@Named
@SessionScoped
public class MovimentosBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4257694980462101373L;
	@Inject
	private EntradaService entradaService;
	private Entrada entrada;
	private List<Entrada> entradas;

	public MovimentosBean() {

	}

	public void preRender(Long id) throws NegocioException {
		entradas = new ArrayList<Entrada>();
		entradas = entradaService.listarPorFuncionario(id);
	}

	public String pegarNome() {
		if (!entradas.isEmpty()) {
			return entradas.get(0).getFuncionario().getNome()
					+" - "+ entradas.get(0).getFuncionario().getEmpresa()
							.getNomeFantasia();

		} else {
			return "SEM ATENDIMENTOS";
		}
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public List<Entrada> getEntradas() {
		return entradas;
	}

	public void setEntradas(List<Entrada> entradas) {
		this.entradas = entradas;
	}

}